/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.api.core.v2;

import net.minecraft.util.Mth;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;

/**
 * Soakable entities are things that can get wet. Wetness can increase when in the rain, swimming, or splashed with a water bottle.
 * This is not related to thirst, instead is just how wet an entity is.
 * <p>
 * This class is interface injected into {@link net.minecraft.world.entity.LivingEntity}. Therefore, ALL methods must have a
 * default implementation. Methods that should normally be abstract should throw a {@link NotImplementedException} instead
 * of being declared abstract.
 */
@ApiStatus.NonExtendable
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

        return Mth.clamp(
                ((float) this.thermoo$getWetTicks()) / maxWetness,
                0.0f, 1.0f
        );
    }
}
