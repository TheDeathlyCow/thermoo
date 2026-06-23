/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
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

package com.github.thedeathlycow.thermoo.api.environment.v2.attribute;

import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureRecord;
import com.github.thedeathlycow.thermoo.impl.CodecHelper;
import com.github.thedeathlycow.thermoo.impl.environment.attribute.ModifierLibraries;
import com.github.thedeathlycow.thermoo.impl.environment.attribute.TemperatureModifier;
import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.AttributeType;

import java.util.Optional;

/**
 * Thermoo analogue of {@link net.minecraft.world.attribute.AttributeTypes}.
 * <p>
 * Provides the attribute type definitions for Thermoo's custom environment attributes.
 */
public final class ThermooAttributeTypes {
    /**
     * An attribute type that holds an optional temperate season. This attribute type is not interpolated.
     * <p>
     * This attribute type has no defined modifier operation.
     */
    public static final AttributeType<Optional<TemperateSeason>> TEMPERATE_SEASON = AttributeType.ofNotInterpolated(
            CodecHelper.optionalCodec(TemperateSeason.CODEC)
    );

    /**
     * An attribute type that holds an optional tropical season. This attribute type is not interpolated.
     * <p>
     * This attribute type has no defined modifier operation.
     */
    public static final AttributeType<Optional<TropicalSeason>> TROPICAL_SEASON = AttributeType.ofNotInterpolated(
            CodecHelper.optionalCodec(TropicalSeason.CODEC)
    );

    /**
     * An attribute type that holds a temperature record. This attribute type is interpolated.
     * <p>
     * It may be modified with the operations add, subtract, minimum, and maximum.
     */
    public static final AttributeType<TemperatureRecord> TEMPERATURE = AttributeType.ofInterpolated(
            TemperatureRecord.CODEC,
            ModifierLibraries.TEMPERATURE_RECORD,
            TemperatureModifier::lerp
    );

    /**
     * An attribute type that holds a double that may not be less than 0. This attribute type is interpolated.
     * <p>
     * It may be modified with all the same operations as {@link net.minecraft.world.attribute.AttributeTypes#FLOAT}.
     */
    public static final AttributeType<Double> POSITIVE_DOUBLE = AttributeType.ofInterpolated(
            Codec.doubleRange(0.0, Double.MAX_VALUE),
            ModifierLibraries.DOUBLE,
            Mth::lerp
    );

    private ThermooAttributeTypes() {

    }
}