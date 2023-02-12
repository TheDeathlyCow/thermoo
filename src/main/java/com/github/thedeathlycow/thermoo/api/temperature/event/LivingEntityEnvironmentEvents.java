package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

/**
 * Events relevant to entity ticking in the environment
 */
public final class LivingEntityEnvironmentEvents {

    private LivingEntityEnvironmentEvents() {
    }

    /**
     * Invoked when an entity is located in a heated location
     */
    public static final Event<TemperatureChangeEventCallback> TICK_IN_HEATED_LOCATION = EventFactory.createArrayBacked(TemperatureChangeEventCallback.class,
            callbacks -> (controller, entity, temperatureChange, result) -> {
                for (TemperatureChangeEventCallback event : callbacks) {
                    event.onTemperatureChange(controller, entity, temperatureChange, result);
                }
            }
    );

    /**
     * Invoked when an entity is affected by a heating effect, such as being on fire.
     */
    public static final Event<TemperatureChangeEventCallback> TICK_HEAT_EFFECT_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(TemperatureChangeEventCallback.class,
            callbacks -> (controller, entity, temperatureChange, result) -> {
                for (TemperatureChangeEventCallback event : callbacks) {
                    event.onTemperatureChange(controller, entity, temperatureChange, result);
                }
            }
    );


    /**
     * Invoked when an entity is in a wet location
     */
    public static final Event<SoakChangeEventCallback> TICK_IN_WET_LOCATION = EventFactory.createArrayBacked(SoakChangeEventCallback.class,
            callbacks -> (controller, entity, soakChange, result) -> {
                for (SoakChangeEventCallback event : callbacks) {
                    event.onSoakChange(controller, entity, soakChange, result);
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
         * @param controller        The {@link EnvironmentController} relevant to this event
         * @param entity            The entity affected by this event
         * @param temperatureChange The temperature change to be applied
         * @param result            Contains whether or not the change has been applied
         */
        void onTemperatureChange(EnvironmentController controller, LivingEntity entity, int temperatureChange, EnvironmentChangeResult result);

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
         * @param soakChange The soak change to be applied
         * @param result     Contains whether or not the change has been applied
         */
        void onSoakChange(EnvironmentController controller, LivingEntity entity, int soakChange, EnvironmentChangeResult result);

    }

}
