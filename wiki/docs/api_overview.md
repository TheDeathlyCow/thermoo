---
title: 📖 API Overview
---
# API Overview

Thermoo is a temperature and environment library for the Fabric and Quilt modding platforms. This page will provide a basic overview of how to set up and interact with the library in your mod or datapack. As of 1.21, Thermoo is required to be installed on both the client and server in order to function properly.

This is a rather long page, so it may be best to follow along in your own mod/datapack while reading, or to skim the page first. There is also a sidebar to help with navigation! Remember that I also have a [Discord](https://discord.gg/aqASuWebRU) where I am happy to help and answer questions related to Thermoo.

## Setup

See the [Setup](./setup.md) page if this is your first time using Thermoo.

## Entity Temperature

Thermoo tracks entity temperature as an integer value that is stored on all living entities (that is, entities with
health, attributes, etc). This value is the number of ticks that the entity has been exposed to temperature. Positive
values indicate that the entity is warm, and negative values indicate that the entity is cold. An entity with a
temperature of `0` is both warm and cold. You can think of this as being similar to the vanilla `TicksFrozen` value, but
inverted to allow for high temperatures.

### Getting and Setting Temperature

In mods, temperature is accessed through the `TemperatureAware` interface, which is implemented on `LivingEntity`
through interface injection.

For datapacks, temperature can be accessed through a command.


=== "Mods (Java)"
    ```java
    void foo(LivingEntity entity) {
        // Methods are automatically injected onto LivingEntity by mixin
        int temperature = entity.thermoo$getTemperature();
    
        entity.thermoo$setTemperature(0);
    }
    ```
=== "Mods (Kotlin)"
    ```kotlin
    // Special Kotlin-only extensions that remove the thermoo$ prefix
    import com.github.thedeathlycow.thermoo.kt.TemperatureAwareExtensions
    
    fun foo(entity: LivingEntity) {
        // Using field extensions
        val temperature: Int = entity.temperature
        entity.temperature = 0
    
        // Without field extensions, you still use the underlying methods.
        // Methods are automatically injected onto LivingEntity by mixin
        val temperature: Int = entity.`thermoo$getTemperature`()
        entity.`thermoo$setTemperature`(0)
    }
    ```
=== "Datapacks (Commands)"
    ```mcfunction
    # All Thermoo commands are technically sub-commands of /thermoo
    # Only displays in chat
    thermoo temperature get @s current
    
    # The result is returned by the command can be stored in all normal places 
    execute store result score Temperature PlayerData run thermoo temperature get @s current
    
    thermoo temperature set @s 0
    
    # Setting by variable requires a macro
    $thermoo temperature set @s $(temperature_in)
    ```

### Minimum and Maximum Temperature and Scale

The entity temperature value has a minimum and maximum value that controls how cold/hot they can get. Whenever an
entity's temperature is updated, the new temperature value will be clamped to its minimum and maximum temperature range.

The minimum and maximum temperature values of an entity are set by attributes. There is an attribute for minimum
temperature (`thermoo:min_temperature`) and maximum temperature (`thermoo:max_temperature`). The final max/min
temperature values are calculated as `140` times the max temperature attribute value, or `-140` times the min
temperature attribute value.<sup>1</sup> The minimum (and default) value of these attributes in Thermoo is `0`, but
mods (like Frostiful and Scorchful) may change this. For example, an entity with a `thermoo:min_temperature` attribute
value of `45` will have a minimum temperature of `-6300`.

!!! info
    In Thermoo 4.x and below (for Minecraft 1.21.1 and below) all Thermoo attributes followed the convention of having a `generic` prefix in their name. For example, `thermoo:generic.min_temperature`. In Thermoo 5.0 and above (for Minecraft 1.21.2+), this was removed. Please use the appropriate ID for your version!

A temperature scale is also available to be queried, based on the entity's current temperature and max/min temperature
values. The temperature scale is a percentage value in the range `[-1, 1]` that represents the entity's current
temperature value divided by their maximum temperature (if warm), or by their minimum temperature (if cold). This scale
is frequently used in [temperature effects](./datapacks/temperature_effect_definition.md) and is often used when
determining exactly how "cold" an entity is.

#### Usage Example

=== "Mods (Java)"
    ```java
    void foo(LivingEntity entity) {
        // Returns the final max temperature value (140 * attribute value)
        int maxTemperature = entity.thermoo$getMaxTemperature();
    
        // Getting the actual attribute value
        double maxTemperatureAttribute = entity.getAttributeValue(ThermooAttributes.MAX_TEMPERATURE);
    
        // Percentage in range [-1, 1], frequently used for temperature effects
        float scale = entity.thermoo$getTemperatureScale();
    }
    ```
=== "Mods (Kotlin)"
    ```kotlin
    import com.github.thedeathlycow.thermoo.kt.TemperatureAwareExtensions
    
    fun foo(entity: LivingEntity) {
        // Returns the final max temperature value (140 * attribute value)
        val maxTemperature: Int = entity.maxTemperature
    
        // Getting the actual attribute value
        val maxTemperatureAttribute: Double = entity.getAttributeValue(ThermooAttributes.MAX_TEMPERATURE)
    
        // Percentage in range [-1, 1], frequently used for temperature effects
        val scale: Float = entity.minTemperature
    }
    ```
=== "Datapacks (Commands)"
      ```mcfunction
      # Returns the final max temperature value (140 * attribute value)
      thermoo temperature get @s max
      
      # Getting the actual attribute value
      attribute @s thermoo:max_temperature get
      
      # Multiples the [-1, 1] scale percentage by the `scale` argument to obtain a readable integer value for commands (default: 100)
      thermoo temperature get @s scale [<scale>]
      ```

---

<sup>1</sup> The value `140` was chosen as the multiplier for min/max temperature as it is the vanilla maximum value for
the `TicksFrozen` attribute, so think of the attributes as multipliers of that vanilla maximum.

### Temperature Changes and Resistances

Usually, when an entity's temperature is increased or decreased, it is done through an `add` procedure, rather than by
directly setting a new value. This `add` procedure is what applies temperature _resistances_ so that mobs and players
can counter environment exposure. These resistances are also controlled by attributes.

When reducing temperature, the attribute `thermoo:frost_resistance` is applied. Similarly, when increasing temperature,
the attribute `thermoo:heat_resistance` is applied. Each point of these attributes correspond to a `10%` reduction of
incoming temperature change. So a Frost Resistance value of 1 will reduce the power temperature decreases by 10%, and a
Heat Resistance value of 10 will reduce the power of temperature increases by 100%. These values can also be negative,
making their corresponding temperature change _worse_.

However, these resistances are only applied based on the _mode_ of the temperature change. Thermoo provides four of
these heating modes: `absolute`, `active`, `passive`, and `environment`.

- The `absolute` mode is the default heating mode, and does not apply and resistances, it would be equivalent to
  applying a method like `set(current + change)`. For most gameplay purposes, however, the `absolute` mode should not be
  used.
- The `active` mode _always_ applies the corresponding resistance to the incoming change. This mode should be used when
  some sort of targeted effect is applied to the entity, such as a freezing spell or being set on fire.
- The `passive` mode only applied the resistance when the target's temperature is in the relevant temperature range. For
  example, frost resistance will be applied to a temperature decrease only when the target is already cold. This mode
  should be used when dealing with temperature changes from nearby blocks, like standing on Ice or the heat of a
  Campfire.
- The `environment` mode is an internal mode that applies the environment resistance
  attributes, `thermoo:environment_frost_resistance` and `thermoo:environment_heat_resistance`, to temperature changes
  from the Environment API (see below). This mode is only used internally and cannot be accessed in the public API.

#### Usage Examples

Adding 10 temperature per tick when on fire, always applying heat resistance.

=== "Mods (Java)"
    ```java
    void tick(LivingEntity entity) {
        if (entity.isOnFire()) {
            entity.thermoo$addTemperature(10, HeatModes.ACTIVE);
        }
    }
    ```
=== "Mods (Kotlin)"
    ```kotlin
    import com.github.thedeathlycow.thermoo.kt.TemperatureAwareExtensions
    
    fun tick(entity: LivingEntity) {
        if (entity.onFire) {
            entity.addTemperature(10, mode = HeatingMods.ACTIVE)
    
            // Beware! This will be an absolute change and will NOT apply resistances
            entity.temperature += 10
        }
    }
    ```
=== "Datapacks (Commands)"
    ```mcfunction
    # Makes this function run every tick
    schedule function example:this_function 1t replace
    
    execute if predicate example:is_on_fire run thermoo temperature add @s 10 active
    ```

Remove 10 temperature per tick when in powder snow, passively applying Frost Resistance

=== "Mods (Java)"
    ```java
    void tick(LivingEntity entity) {
        // yes this is a field
        if (entity.inPowderSnow || entity.wasInPowderSnow) {
            entity.thermoo$addTemperature(-10, HeatModes.PASSIVE);
        }
    }
    ```
=== "Mods (Kotlin)"
    ```kotlin
    import com.github.thedeathlycow.thermoo.kt.TemperatureAwareExtensions
    
    fun tick(entity: LivingEntity) {
        // yes this is a field
        if (entity.inPowderSnow || entity.wasInPowderSnow) {
            entity.addTemperature(-10, mode = HeatModes.PASSIVE)
        }
    }
    ```
=== "Datapacks (Commands)"
    ```mcfunction
    # Makes this function run every tick
    schedule function example:this_function 1t replace
    
    execute if block ~ ~ ~ minecraft:powder_snow run thermoo temperature remove @s 10 passive
    ```

<br/>

## Soaking and Wetness

Similar to temperature, wetness is also a tracked value for living entities in Thermoo. Wetness is an integer value that
has a minimum value of `0` and a default maximum value of `600`. This is just a simple tracker for how many ticks an
entity has been exposed to water.

The Soaking max value can be changed with the attribute `thermoo:max_soaking_tick_multiplier`. As the name says, this
attribute is a multiplier of the base value of `600`, so the final max wet ticks value
equals `floor(600 * valueOf(max_soaking_tick_multiplier))`.

For modders, wetness is exposed through the `Soakable` interface, which is injected onto `LivingEntity` by mixin just
like `TemperatureAware`.

For datapack authors, wetness can be interacted with through the command `/thermoo soaking`.

### Usage Example

=== "Mods (Java)"
    ```java
    void foo(LivingEntity entity) {
        // getting current wet ticks
        int wetTicks = entity.thermoo$getWetTicks();
    
        // setting wet ticks to the max value
        int max = entity.thermoo$getMaxWetTicks();
        entity.thermoo$setWetTicks(max);
    
        // getting 0-1 percentage scale of wetness
        float soakedScale = entity.thermoo$getSoakedScale();
    }
    ```
=== "Mods (Kotlin)"
    ```kotlin
    // Similar Kotlin-only extensions as TemperatureAware
    import com.github.thedeathlycow.thermoo.api.kt.SoakableExtensions
    
    fun foo(entity: LivingEntity) {
        // getting current wet ticks
        val wetTicks: Int = entity.wetTicks
    
        // setting wet ticks to the max value
        entity.wetTicks = entity.maxWetTicks
    
        // getting 0-1 percentage scale of wetness
        val soakedScale: Float = entity.soakedScale
    }
    ```
=== "Datapacks (Commands)"
    ```mcfunction
    # Getting current wet ticks
    thermoo soaking get @s current
    
    # Setting wet ticks to the default max value
    thermoo soaking set @s 600
    
    # Multiples the [-1, 1] soaked scale percentage by the `scale` argument to obtain a readable integer value for commands (default: 100)
    thermoo soaking get @s scale [<scale>]
    ```

<br/>

## Temperature Effects

Temperature Effects are a Datapack registry that can be used to apply various kinds of effects to entities based on
their current temperature and other conditions each tick. They are used for both mods and datapacks, and can be used in
place of hard-coded tick methods and listeners. The full format is documented on
the [Temperature Effect page](./datapacks/temperature_effect_definition.md).

### Usage example

Applies Mining Fatigue to cold players

```json title="data/example/thermoo/temperature_effect/status_effect.json"
{
    "type": "thermoo:status_effect",
    "entity_type": "minecraft:player",
    "temperature_scale_range": {
        "max": -0.5
    },
    "config": {
        "effects": [
            {
                "amplifier": 0,
                "duration": 20,
                "effect": "minecraft:mining_fatigue"
            }
        ]
    }
}
```

<br/>

## Environment API

The Environment API sets and defines the parameters of a player's current environment and converts these to changes in
temperature exposure. This is a large API that interfaces between both mods and datapacks and with many of the other
APIs provided by Thermoo, so don't worry if you get a little overwhelmed. I also have
a [Discord](https://discord.gg/aqASuWebRU) where I am happy to answer questions or provide assistance as needed.

### Environment Components

Environment parameters are provided as a set of components (just like items!) that are set by an _environment provider_
for a world position. Thermoo will update and provide these values to a mod-only set of events for every player each
tick. These parameters can then be converted into temperature point changes and other effects. This is a large API with
many complicated parts, so examples can be helpful.
The [Thermoo Test Mod](https://github.com/TheDeathlyCow/thermoo/tree/1.21.3/src/testmod/resources/data/thermoo-test/thermoo)
provides some examples, but the best examples of it in practice can be found
in [Frostiful](https://github.com/TheDeathlyCow/frostiful/tree/1.21.1/src/main/resources/data/frostiful/thermoo)
and [Scorchful](https://github.com/TheDeathlyCow/scorchful/tree/1.21.1/src/main/resources/data/scorchful/thermoo).

Thermoo currently provides two environment component types: `thermoo:temperature` and `thermoo:relative_humidity`. The
Temperature component is stored in a special [Temperature Record](./mods/temperature_unit.md) object that contains a
temperature value and a unit (such as Celsius or Fahrenheit), which records the current temperature of the block
position. The relative humidity is a 0-1 percentage of the area's
current [relative humidity](https://en.m.wikipedia.org/wiki/Humidity#Relative_humidity). Custom component types may be
added by mods to the registry `ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE`.

### Environment Definition and Environment Providers

The environment has two main datapack registries for environment parameters: the Environment registry, and the
Environment Provider registry. The Environment registry is what makes the parameters visible to Thermoo, and entries in
the Environment Provider registry are what actually calculate the parameters.

Entries for the Environment and Environment Provider registries go in the folders `thermoo/environment`
and `thermoo/environment_provider` respectively. However, Environment Providers can, and often are, in-lined to
Environments directly.

In order for Thermoo to detect your set of environment parameters, you must define the environment for a set of biomes
in the datapack registry `thermoo/environment`. This includes the provider, the biomes the environment provides for, and
the biomes the environment _does not_ provide for. As a rule of thumb, it is best for each biome to only have one
environment for it at a time, as multiple environments will overwrite each other based on load order which may lead to
inconsistent behaviour.

There are many types of environment providers, which work like a tree with many different branch provider types that
select a child provider based on the current state of some parameter (like season, light level, or weather conditions).
The `thermoo:constant` provider just returns a constant set of parameters, and is the primary "leaf" provider.

Much like custom components, mods can also define custom provider types to the
registry `ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE`.

The full format for these registries can be found on the [Environment Definition](./datapacks/environment_definition.md)
and [Environment Provider](./datapacks/environment_provider_definition.md) pages.

#### Usage Examples

Defining the temperature for snowy biomes based on the [season](./mods/seasons.md) (datapack-only).

```json5 title="data/example/thermoo/environment/snowy_temperature.json"
// Note: comments are not allowed in the actual JSON files
{
    // This is the environment definition
    "biomes": "#c:is_snowy", // apply to snowy biomes...
    "exclude_biomes": "#c:is_icy", // ...but not icy biomes
    "provider": {
        // everything here is now an in-line defined environment provider
        "type": "thermoo:seasonal/temperate",
        "fallback_season": "spring",
        "seasons": {
            "spring": "example:freezing", // reference another provider
            "summer": { // or inline the provider
                "type": "thermoo:constant",
                "components": {
                    "thermoo:temperature": 10
                }
            },
            "autumn": "example:freezing",
            "winter": {
                "type": "thermoo:constant",
                "components": {
                    "thermoo:temperature": -10
                }
            }
        }
    }
}
```

This is an environment provider defined in its own file.

```json title="data/example/thermoo/environment_provider/freezing.json"
{
    "type": "thermoo:constant",
    "components": {
        "thermoo:temperature": 0.0
    }
}
```

### Environment Lookup

Once you define your environment, you can see what the current parameters are through the lookup API. Interfaces are
provided for both mods and datapack commands. However, datapack commands are hard-coded to only work for the basic
component types provided by Thermoo - mods that add other component types will need to define their own lookup commands
for them.

=== "Mods (Java)"
    ```java
    void foo(World world, BlockPos pos) {
        EnvironmentLookup lookup = EnvironmentLookup.getInstance();
    
        ComponentMap components = look.findEnvironmentComponents(world, pos);
    
        TemperatureRecord temperature = components.getOrDefault(
                EnvironmentComponentTypes.TEMPERATURE,
                TemperatureRecordComponent.DEFAULT
        );
        LOGGER.info("The current temperature is {}", temperature);
    }
    ```
=== "Mods (Kotlin)"
    ```kotlin
    fun foo(world: World, pos: BlockPos) {
        val lookup: EnvironmentLookup = EnvironmentLookup.getInstance()
    
        val components: ComponentMap = look.findEnvironmentComponents(world, pos)
    
        val temperature: TemperatureRecord = components.getOrDefault(
            EnvironmentComponentTypes.TEMPERATURE,
            TemperatureRecordComponent.DEFAULT
        )
        LOGGER.info("The current temperature is {}", temperature)
    }
    ```
=== "Datapacks (Commands)"
    ```mcfunction
    # Default unit is Celsius, but can also be Fahrenheit, Kelvin, or Rankine
    thermoo environment temperature ~ ~ ~ [<unit>]
    
    # Scale multiples the 0-1 relative humidity scale to a readable int value for commands. Default: 100
    thermoo environment relativehumidity ~ ~ ~ [<scale>]
    ```

### Tick Events

Once the environment parameters are set, they are passed to Thermoo's various tick events. Listeners can then convert
these parameters to temperature point changes for the tick, or perform other effects. Currently, environment parameters
are only defined by Thermoo for players, even in events that are not exclusive to players. However, components can be
looked up manually at any time using the `EnvironmentLookup` interface.

The tick event are currently given in the classes `LivingEntityTemperatureTickEvents`, `LivingEntitySoakingTickEvents`,
and `ServerPlayerEnvironmentTickEvents` (for players only). See the javadoc of these classes for more specific
information on each event and its parameters.

#### Usage Examples

Freezing players when the temperature is low (mod-only)

=== "Java"
    ```java
    static final TemperatureRecord FREEZING = new TemperatureRecord(0, TemperatureUnit.CELSIUS);
    
    static void initialize() {
        ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.register(context -> {
            TemperatureRecord temperature = context.components().getOrDefault(
                    EnvironmentComponentTypes.TEMPERATURE,
                    TemperatureRecordComponent.DEFAULT
            );
            return temperature.compareTo(FREEZING) <= 0 ? -1 : 0;
        });
    }
    ```
=== "Kotlin"
    ```kotlin
    import com.github.thedeathlycow.thermoo.api.kt.TemperatureRecordExtensions
    
    fun initialize() {
        ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.register { context ->
            val temperature = context.components().getOrDefault(
                EnvironmentComponentTypes.TEMPERATURE,
                TemperatureRecordComponent.DEFAULT
            )
            return@register if (temperature <= 0.C) -1 else 0
        }
    }
    ```
