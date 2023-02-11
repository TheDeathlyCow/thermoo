package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public final class LivingEntityEnvironmentEvents {

    private LivingEntityEnvironmentEvents() {
    }

    public static final Event<EnvironmentChangEvent> TICK_IN_HEATED_LOCATED = EventFactory.createArrayBacked(EnvironmentChangEvent.class,
            callbacks -> (controller, entity, temperatureChange, result) -> {
                for (EnvironmentChangEvent event : callbacks) {
                    event.onTemperatureChange(controller, entity, temperatureChange, result);
                }
            }
    );

    public static final Event<EnvironmentChangEvent> TICK_HEAT_EFFECT_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(EnvironmentChangEvent.class,
            callbacks -> (controller, entity, temperatureChange, result) -> {
                for (EnvironmentChangEvent event : callbacks) {
                    event.onTemperatureChange(controller, entity, temperatureChange, result);
                }
            }
    );


    public static final Event<EnvironmentChangEvent> TICK_IN_WET_LOCATION = EventFactory.createArrayBacked(EnvironmentChangEvent.class,
            callbacks -> (controller, entity, soakChange, result) -> {
                for (EnvironmentChangEvent event : callbacks) {
                    event.onTemperatureChange(controller, entity, soakChange, result);
                }
            }
    );

    @FunctionalInterface
    public interface EnvironmentChangEvent {

        void onTemperatureChange(EnvironmentController controller, LivingEntity entity, int environmentStatChange, EnvironmentChangeResult result);

    }

}
