package com.github.thedeathlycow.thermoo.api.environment.attribute;

import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasonState;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.impl.environment.attribute.TemperatureModifier;
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
    public static final AttributeType<SeasonStateAttribute<TemperateSeason>> TEMPERATE_SEASON = AttributeType.ofNotInterpolated(
            SeasonStateAttribute.codec(TemperateSeason.CODEC)
    );

    /**
     * An attribute type that holds an optional tropical season. This attribute type is not interpolated.
     * <p>
     * This attribute type has no defined modifier operation.
     */
    public static final AttributeType<SeasonStateAttribute<TropicalSeason>> TROPICAL_SEASON = AttributeType.ofNotInterpolated(
            SeasonStateAttribute.codec(TropicalSeason.CODEC)
    );

    /**
     * An attribute type that holds a temperature record. This attribute type is interpolated.
     * <p>
     * It may be modified with the operations add, subtract, minimum, and maximum.
     */
    public static final AttributeType<TemperatureRecord> TEMPERATURE = AttributeType.ofInterpolated(
            TemperatureRecord.CODEC,
            TemperatureModifier.TEMPERATURE_RECORD_LIBRARY,
            TemperatureModifier::lerp
    );

    private ThermooAttributeTypes() {

    }
}