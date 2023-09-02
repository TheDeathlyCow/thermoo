package com.github.thedeathlycow.thermoo.api.temperature.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Events relevant to player ticking and passive temperature changes
 */
public final class PlayerEnvironmentEvents {

    /**
     * Called to check that a player is vulnerable to passive temperature changes.
     * <p>
     * If any listener returns false, then further processing is cancelled and the event will return false.
     */
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

        /**
         * Checks that a player can have the given {@code change} applied as a passive temperature change.
         *
         * @param change The passive temperature change to be applied
         * @param player The player to check
         * @return Returns {@code true} if this callback will allow for the {@code change} to be applied to the
         * {@code player}. If this returns {@code false}, further processing is cancelled and the change is not applied.
         */
        boolean canApplyChange(int change, PlayerEntity player);

    }

    private PlayerEnvironmentEvents() {
    }

}
