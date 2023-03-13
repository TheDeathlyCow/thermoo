package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.profiler.Profiler;

/**
 * Events relevant to entity ticking in the environment
 */
public final class LivingEntityEnvironmentEvents {

    private LivingEntityEnvironmentEvents() {
    }

    /**
     * Invoked on each entity in a heated location
     */
    public static final Event<TemperatureChangeEventCallback> TICK_IN_HEATED_LOCATION = EventFactory.createArrayBacked(TemperatureChangeEventCallback.class,
            callbacks -> (controller, entity, result) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = entity.world.getProfiler();
                    profiler.push("thermooHeatedLocationTick");

                    for (TemperatureChangeEventCallback event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onTemperatureChange(controller, entity, result);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (TemperatureChangeEventCallback event : callbacks) {
                        event.onTemperatureChange(controller, entity, result);
                    }
                }
            }
    );

    /**
     * Invoked on each living entity every tick, primarily for the purpose of tracking temperature changes
     * from entity-related effects such as being on fire.
     * <p>
     * The result will contain the on fire warmth change as its initial change; however this value will be 0 if the
     * entity is not on fire.
     * <p>
     * Is not invoked on spectators
     */
    public static final Event<TemperatureChangeEventCallback> TICK_HEAT_EFFECTS = EventFactory.createArrayBacked(TemperatureChangeEventCallback.class,
            callbacks -> (controller, entity, result) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = entity.world.getProfiler();
                    profiler.push("thermooHeatEffectsTick");

                    for (TemperatureChangeEventCallback event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onTemperatureChange(controller, entity, result);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (TemperatureChangeEventCallback event : callbacks) {
                        event.onTemperatureChange(controller, entity, result);
                    }
                }
            }
    );


    /**
     * Invoked when an entity is in a wet location
     * <p>
     * Is not invoked on spectators
     */
    public static final Event<SoakChangeEventCallback> TICK_IN_WET_LOCATION = EventFactory.createArrayBacked(SoakChangeEventCallback.class,
            callbacks -> (controller, entity, result) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = entity.world.getProfiler();
                    profiler.push("thermooWetLocationTick");

                    for (SoakChangeEventCallback event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onSoakChange(controller, entity, result);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (SoakChangeEventCallback event : callbacks) {
                        event.onSoakChange(controller, entity, result);
                    }
                }
            }
    );

    /**
     * Event callback for temperature change ticks
     */
    @FunctionalInterface
    public interface TemperatureChangeEventCallback {

        /**
         * Invoked when the temperature change should be applied. Note that the change is NOT applied by this event,
         * listeners must apply it themselves.
         *
         * @param controller The {@link EnvironmentController} relevant to this event
         * @param entity     The entity affected by this event
         * @param result     Stores information about the change to be applied
         */
        void onTemperatureChange(EnvironmentController controller, LivingEntity entity, EnvironmentChangeResult result);

    }

    /**
     * Event callback for soak change ticks
     */
    @FunctionalInterface
    public interface SoakChangeEventCallback {

        /**
         * Invoked when the soak change should be applied. Note that the change is NOT applied by this event,
         * listeners must apply it themselves.
         *
         * @param controller The {@link EnvironmentController} relevant to this event
         * @param entity     The entity affected by this event
         * @param result     Stores information about the change to be applied
         */
        void onSoakChange(EnvironmentController controller, LivingEntity entity, EnvironmentChangeResult result);

    }

}
