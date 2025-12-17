package com.github.thedeathlycow.thermoo.api.environment.attribute;

import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasonState;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
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
    public static final EnvironmentAttribute<Optional<ThermooSeasonState<TemperateSeason>>> TEMPERATE_SEASON = EnvironmentAttribute.builder(ThermooAttributeTypes.TEMPERATE_SEASON)
            .defaultValue(Optional.empty())
            .build();

    /**
     * An environment attribute that stores a tropical season. By default, this attribute is empty.
     *
     * @see ThermooAttributeTypes#TROPICAL_SEASON
     */
    public static final EnvironmentAttribute<Optional<ThermooSeasonState<TropicalSeason>>> TROPICAL_SEASON = EnvironmentAttribute.builder(ThermooAttributeTypes.TROPICAL_SEASON)
            .defaultValue(Optional.empty())
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