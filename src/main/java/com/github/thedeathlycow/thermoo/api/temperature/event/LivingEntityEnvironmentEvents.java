package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public final class LivingEntityEnvironmentEvents {

    private LivingEntityEnvironmentEvents() {
    }

    public static final Event<TemperatureChangeEvent> TICK_IN_HEATED_LOCATION = EventFactory.createArrayBacked(TemperatureChangeEvent.class,
            callbacks -> (controller, entity, temperatureChange, result) -> {
                for (TemperatureChangeEvent event : callbacks) {
                    event.onTemperatureChange(controller, entity, temperatureChange, result);
                }
            }
    );

    public static final Event<TemperatureChangeEvent> TICK_HEAT_EFFECT_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(TemperatureChangeEvent.class,
            callbacks -> (controller, entity, temperatureChange, result) -> {
                for (TemperatureChangeEvent event : callbacks) {
                    event.onTemperatureChange(controller, entity, temperatureChange, result);
                }
            }
    );


    public static final Event<SoakChangeEvent> TICK_IN_WET_LOCATION = EventFactory.createArrayBacked(SoakChangeEvent.class,
            callbacks -> (controller, entity, soakChange, result) -> {
                for (SoakChangeEvent event : callbacks) {
                    event.onSoakChange(controller, entity, soakChange, result);
                }
            }
    );

    @FunctionalInterface
    public interface TemperatureChangeEvent {

        void onTemperatureChange(EnvironmentController controller, LivingEntity entity, int temperatureChange, EnvironmentChangeResult result);

    }

    @FunctionalInterface
    public interface SoakChangeEvent {

        void onSoakChange(EnvironmentController controller, LivingEntity entity, int soakChange, EnvironmentChangeResult result);

    }

}
