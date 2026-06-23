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

package com.github.thedeathlycow.thermoo.api.core.v2.source;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooBuiltInRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

/**
 * A method for reducing or adjusting temperature changes. Built in implementations currently use attributes to adjust
 * temperature.
 */
public interface TemperatureReduction {
    Codec<TemperatureReduction> DIRECT_CODEC = ThermooBuiltInRegistries.TEMPERATURE_REDUCTION_TYPE.byNameCodec()
            .dispatch(TemperatureReduction::codec, Function.identity());

    /**
     * Applies a reduction in the temperature change to the target.
     *
     * @param target            The target being affected by the temperature change.
     * @param context           The context of the temperature change.
     * @param temperatureChange The amount of the temperature change.
     * @return Returns an adjusted temperature change.
     */
    int applyReduction(LivingEntity target, TemperatureChange context, int temperatureChange);

    /**
     * The codec for the reduction type. Implementors should register this codec to
     * {@link ThermooBuiltInRegistries#TEMPERATURE_REDUCTION_TYPE} in an entry point.
     */
    MapCodec<? extends TemperatureReduction> codec();
}