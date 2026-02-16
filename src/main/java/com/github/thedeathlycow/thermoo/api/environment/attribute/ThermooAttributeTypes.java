package com.github.thedeathlycow.thermoo.api.environment.attribute;

import com.github.thedeathlycow.thermoo.api.environment.component.AtmosphericPressureComponent;
import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
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

    public static final AttributeType<Double> POSITIVE_DOUBLE = AttributeType.ofInterpolated(
            Codec.doubleRange(0.0, Double.MAX_VALUE),
            ModifierLibraries.DOUBLE,
            Mth::lerp
    );

    private ThermooAttributeTypes() {

    }
}