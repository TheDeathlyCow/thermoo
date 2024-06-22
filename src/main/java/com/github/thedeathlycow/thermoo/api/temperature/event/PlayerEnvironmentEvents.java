package com.github.thedeathlycow.thermoo.api.temperature.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Events relevant to player ticking and passive temperature changes
 */
public final class PlayerEnvironmentEvents {

    /**
     * Called to check that a player is vulnerable to passive temperature changes.
     * <p>
     * If any listener returns anything other than {@link TriState#DEFAULT}, then further processing is cancelled
     * and the event will return that value.
     * <p>
     * By default, this event returns {@link TriState#TRUE}
     */
    public static final Event<TemperatureChangeEventCallback> CAN_APPLY_PASSIVE_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            TemperatureChangeEventCallback.class,
            callbacks -> (change, player) -> {
                for (TemperatureChangeEventCallback event : callbacks) {
                    TriState result = event.canApplyChange(change, player);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }

                return TriState.TRUE;
            }
    );

    @FunctionalInterface
    public interface TemperatureChangeEventCallback {

        /**
         * Checks that a player can have the given {@code change} applied as a passive temperature change.
         *
         * @param change The passive temperature change to be applied
         * @param player The player to check
         * @return Returns {@link TriState#TRUE} or {@link TriState#FALSE} if this callback should allow (or not allow)
         * for the {@code change} to be applied to the {@code player}. If this returns {@link TriState#FALSE}, falls back
         * to further processing.
         */
        TriState canApplyChange(int change, PlayerEntity player);

    }

    private PlayerEnvironmentEvents() {
    }

}
