package com.github.thedeathlycow.thermoo.api.temperature.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Events relevant to player ticking and passive temperature changes
 */
public final class PlayerEnvironmentEvents {

    private PlayerEnvironmentEvents() {
    }

    public static final Event<TemperatureChangeEventCallback> CAN_APPLY_PASSIVE_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            TemperatureChangeEventCallback.class,
            callbacks -> (change, player) -> {
                for (TemperatureChangeEventCallback event : callbacks) {
                    if (!event.canApplyChange(change, player)) {
                        return false;
                    }
                }

                return true;
            }
    );

    @FunctionalInterface
    public interface TemperatureChangeEventCallback {

        boolean canApplyChange(int change, PlayerEntity player);

    }


}
