package com.github.thedeathlycow.thermoo.api.temperature.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;

/**
 * Events related to non-environmental temperature changes on {@link net.minecraft.entity.LivingEntity}s
 */
public final class LivingEntityTemperatureEvents {

    private LivingEntityTemperatureEvents() {
    }

    /**
     * An event that checks if the heat change should be applied from hot floor.
     * <p>
     * Event stops its invocation as soon as a single listener returns {@code true}, so it should not be used for ticking
     * behaviours that should always be run.
     */
    public static final Event<OnSteppedOnHotFloor> ON_STEPPED_ON_HOT_FLOOR = EventFactory.createArrayBacked(OnSteppedOnHotFloor.class, callbacks -> (entity, steppedOn, temperatureChange) -> {
        boolean shouldApply;
        for (var callback : callbacks) {
            shouldApply = callback.shouldApplyChange(entity, steppedOn, temperatureChange);

            if (shouldApply) {
                return true;
            }
        }
        return false;
    });


    @FunctionalInterface
    public interface OnSteppedOnHotFloor {

        /**
         * Checks whether a temperature change from hot floors should be applied
         *
         * @param entity            The entity to apply the temperature change to
         * @param steppedOn         The state that the entity stepped on
         * @param temperatureChange The temperature change to apply
         * @return {@code true} if the change should be applied
         */
        boolean shouldApplyChange(LivingEntity entity, BlockState steppedOn, int temperatureChange);

    }
}
