package com.github.thedeathlycow.thermoo.api.temperature;

import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Soakable entities are things that can get wet. Wetness can increase when in the rain, swimming, or splashed with a water bottle.
 * This is not related to thirst, instead is just how wet an entity is.
 * <p>
 * This class is interface injected into {@link net.minecraft.entity.LivingEntity}. Therefore, ALL methods must have a
 * default implementation. Methods that should normally be abstract should throw a {@link NotImplementedException} instead
 * of being declared abstract.
 */
public interface Soakable {

    /**
     * Sets the wet ticks of a soakable to an exact amount.
     * <p>
     * Clamps the amount between 0 and max wet ticks before setting.
     *
     * @param amount The amount of wet ticks
     */
    default void thermoo$setWetTicks(int amount) {
        throw new NotImplementedException();
    }

    /**
     * @return Returns the wet ticks of the soakable
     */
    default int thermoo$getWetTicks() {
        throw new NotImplementedException();
    }

    /**
     * @return Returns the maximum wet ticks the soakable can have
     */
    default int thermoo$getMaxWetTicks() {
        throw new NotImplementedException();
    }

    /**
     * Soakables ignore frigid water if they can breathe in water.
     *
     * @return Returns if the soakable ignores the effects of frigid water
     */
    default boolean thermoo$ignoresFrigidWater() {
        throw new NotImplementedException();
    }

    /**
     * @return Returns if the soakable has a positive number of wet ticks
     */
    default boolean thermoo$isWet() {
        return this.thermoo$getWetTicks() > 0;
    }

    default void thermoo$addWetTicks(int delta) {
        this.thermoo$setWetTicks(this.thermoo$getWetTicks() + delta);
    }

    /**
     * @return Returns if the soakable's current wet ticks is greater than or equal to its maximum wet ticks
     */
    default boolean thermoo$isSoaked() {
        return this.thermoo$getWetTicks() >= this.thermoo$getMaxWetTicks();
    }

    /**
     * @return Returns the current wet ticks of the soakable as a percentage scale of the max wet ticks on a 0-1 scale.
     */
    default float thermoo$getSoakedScale() {
        int maxWetness = this.thermoo$getMaxWetTicks();
        if (maxWetness <= 0) {
            return 0.0f;
        }

        return MathHelper.clamp(
                ((float) this.thermoo$getWetTicks()) / maxWetness,
                0.0f, 1.0f
        );
    }
}
