---
title: 🌎 Environment Controller and Events
status: deprecated
---
# Environment Controller and Events

!!! warning
    This feature is deprecated in Minecraft 1.21.1+ and has been replaced with the new [Environment API](../api_overview.md#environment-api). Please use that instead. This feature will be removed by Thermoo's Minecraft 1.21.5 update, possibly in 1.21.4.

The `EnvironmentController` is an interface that provides configuration and values for wetness change per tick, passive temperature change per tick, block heating, and more. The implementation of this interface can be accessed through the `EnvironmentManager`. By default, all methods in the controller return `0`, `false`, or `null` (depending on the return type of course), unless specified otherwise. In order to get them to do anything, you will need to write your own *decorator* of the controller by extending the `EnvironmentControllerDecorator` class. Your implementation will then need to be registered through the `EnvironmentManager` in order to take effect.

Individual methods are documented in their respective javadoc in the source.

## Extending the Controller

The EnvironmentController implements a decorator pattern that allows for mods to apply their own changes to it dynamically with compatibility in mind. If you want to add your own functionality to the environment controller, you must extend `EnvironmentControllerDecorator` and write your changes there. This decorator allows you to handle previous values created by other controllers, and mods can choose to either use them as the base for further enhancement, or fully overwrite them all together.

Then you register your decorator using `EnvironmentControllerInitializeEvent` in your mod's initializer, with the constructor passed as the listener. For example, `EnvironmentControllerInitializeEvent.EVENT.register(MyController::new)`. In order to have a better sense of decorator application order when considering other mods, the event is implemented with a phased ordering as follows:

1. `Event.DEFAULT_PHASE`: The first and default phase of the event. Use this phase for setting base controller values that may be modified/overwritten later. Generally speaking, you can assume that the controller value will be 0. This phase does not need to be specified when registering event listeners.
2. `EnvironmentControllerInitializeEvent.MODIFY_PHASE`: The second phase of the event. Use this to modify the base values of the decorator.
3. `EnvironmentControllerInitializeEvent.OVERRIDE_PHASE`: The third phase of the event. Use this when you want to explicitly overwrite values from other mods.
4. `EnvironmentControllerInitializeEvent.LISTENER_PHASE`: The final phase of the event. Should not be used to update controller values, only read and react to them. For example, add particles when heating/cooling the player.


UML Class diagram of Environment contoller decorator:
![UML Class diagram of Environment contoller decorator](https://github.com/TheDeathlyCow/thermoo/blob/main/docs/environment-controller.svg)

As an alternative to event registration, you can directly register a controller with `EnvironmentManager#addController`. However, be careful with this as controllers are destroyed and reinitialized whenever the server stops and starts. In general, it is better to use the initialization event, though `addController()` can still be useful in cases where you want to temporarily add a controller and then remove it again with `EnvironmentManager#peelController`, such as mocking out some features of temperature in game tests.

## Events

The only other event as of writing is the `CAN_APPLY_PASSIVE_TEMPERATURE_CHANGE` event in `PlayerEnvironmentEvents`. This event is used to check if a player can or cannot be subjected to a passive temperature change. If any listener returns `false`, all further processing is stopped and the result of the event will be `false`. In order for the event to be triggered, a non-zero temperature change must be computed by `EnvironmentController#getLocalTemperatureChange`.

There are also a few events in the [Extra APIs](https://github.com/TheDeathlyCow/thermoo/wiki/Mod-Usage#extra-apis)

## Examples

### Example: Defining and registering a custom decorator

```java

// Custom decorator class
public class MagmablockFloorDecorator extends EnvironmentControllerDecorator {
    
    public MagmablockFloorDecorator(EnvironmentController controller) {
        super(controller);
    }
    
    @Override
    public int getFloorTemperature(LivingEntity entity, World world, BlockState state, BlockPos pos) {
        if (state.isOf(Blocks.MAGMA_BLOCK)) {
            return 5;
        } else {
            // Otherwise, fallback to base value. Note that the controller field has protected access in EnvironmentControllerDecorator.
            return controller.getFloorTemperature(entity, world, state, pos);
        }
    }
}

// Your mod's primary entry point
public class ExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
         // ... other initialization logic
         // Register in the default phase
         EnvironmentControllerInitializeEvent.EVENT.register(MagmablockFloorDecorator::new);
         // Alternatively: register in the modify phase
         EnvironmentControllerInitializeEvent.EVENT.register(EnvironmentControllerInitializeEvent.MODIFY_PHASE, MagmablockFloorDecorator::new);
    }

}

```

### Example: Mocking Environment Controller for `@GameTest`s

You can create custom environment controllers for use in tests, to prevent some mechanics (such as passive freezing) from interfering with your other tests, but still retain other functionality, like so:

```java
    @BeforeBatch(batchId = "hotFloorTests")
    public void mockController(ServerWorld serverWorld) {
        // Decorates the current controller with a new anonymously-implemented mock controller 
        EnvironmentManager.INSTANCE.addController(
                controller ->
                        new EnvironmentControllerDecorator(controller) {
                            @Override
                            public int getLocalTemperatureChange(World world, BlockPos pos) {
                                return 0;
                            }

                            @Override
                            public int getHeatAtLocation(World world, BlockPos pos) {
                                return 0;
                            }

                            @Override
                            public int getTemperatureEffectsChange(LivingEntity entity) {
                                return 0;
                            }
                        }
        );
    }

    @AfterBatch(batchId = "hotFloorTests")
    public void resetController(ServerWorld serverWorld) {
        // Removes the last controller added (in our case, the mock controller)
        EnvironmentManager.INSTANCE.peelController();
    }

```
