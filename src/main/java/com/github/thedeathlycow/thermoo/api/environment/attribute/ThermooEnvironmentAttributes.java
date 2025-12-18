package com.github.thedeathlycow.thermoo.api.environment.attribute;

import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;

import java.util.Optional;

/**
 * Thermoo analogue for {@link net.minecraft.world.attribute.EnvironmentAttributes}.
 * <p>
 * Defines Thermoo's custom environment attributes.
 */
public final class ThermooEnvironmentAttributes {
    /**
     * An environment attribute that stores a temperate season. By default, this attribute is empty.
     *
     * @see ThermooAttributeTypes#TEMPERATE_SEASON
     */
    public static final EnvironmentAttribute<Optional<TemperateSeason>> TEMPERATE_SEASON = EnvironmentAttribute.builder(ThermooAttributeTypes.TEMPERATE_SEASON)
            .defaultValue(Optional.empty())
            .build();

    /**
     * An environment attribute that stores a tropical season. By default, this attribute is empty.
     *
     * @see ThermooAttributeTypes#TROPICAL_SEASON
     */
    public static final EnvironmentAttribute<Optional<TropicalSeason>> TROPICAL_SEASON = EnvironmentAttribute.builder(ThermooAttributeTypes.TROPICAL_SEASON)
            .defaultValue(Optional.empty())
            .build();

    /**
     * An environment attribute that stores the current progress of a temperate season. This attribute's value is
     * clamped to the range [0, 1] when used, but may take any value permitted by {@link AttributeTypes#FLOAT}.
     * <p>
     * By default, this attribute has a value of 0. It may be interpolated, and modified with the operations alpha blend,
     * add, subtract, multiply, minimum, and maximum.
     *
     * @see AttributeTypes#FLOAT
     */
    public static final EnvironmentAttribute<Float> TEMPERATE_SEASON_PROGRESS = EnvironmentAttribute.builder(AttributeTypes.FLOAT)
            .defaultValue(0f)
            .build();

    /**
     * An environment attribute that stores the current progress of a tropical season. This attribute's value is
     * clamped to the range [0, 1] when used, but may take any value permitted by {@link AttributeTypes#FLOAT}.
     * <p>
     * By default, this attribute has a value of 0. It may be interpolated, and modified with the operations alpha blend,
     * add, subtract, multiply, minimum, and maximum.
     *
     * @see AttributeTypes#FLOAT
     */
    public static final EnvironmentAttribute<Float> TROPICAL_SEASON_PROGRESS = EnvironmentAttribute.builder(AttributeTypes.FLOAT)
            .defaultValue(0f)
            .build();

    /**
     * An environment attribute that stores a temperature record. This attribute defaults to 20°C.
     *
     * @see ThermooAttributeTypes#TEMPERATURE
     */
    public static final EnvironmentAttribute<TemperatureRecord> TEMPERATURE = EnvironmentAttribute.builder(ThermooAttributeTypes.TEMPERATURE)
            .defaultValue(new TemperatureRecord(20, TemperatureUnit.CELSIUS))
            .build();

    private ThermooEnvironmentAttributes() {

    }
}