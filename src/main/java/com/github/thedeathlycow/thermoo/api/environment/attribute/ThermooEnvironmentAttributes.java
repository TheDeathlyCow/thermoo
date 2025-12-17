package com.github.thedeathlycow.thermoo.api.environment.attribute;

import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import net.minecraft.world.attribute.EnvironmentAttribute;

import java.util.Optional;

public final class ThermooEnvironmentAttributes {
    public static final EnvironmentAttribute<Optional<TemperateSeason>> TEMPERATE_SEASON = EnvironmentAttribute.builder(ThermooAttributeTypes.TEMPERATE_SEASON)
            .defaultValue(Optional.empty())
            .build();

    public static final EnvironmentAttribute<Optional<TropicalSeason>> TROPICAL_SEASON = EnvironmentAttribute.builder(ThermooAttributeTypes.TROPICAL_SEASON)
            .defaultValue(Optional.empty())
            .build();

    public static final EnvironmentAttribute<TemperatureRecord> TEMPERATURE = EnvironmentAttribute.builder(ThermooAttributeTypes.TEMPERATURE)
            .defaultValue(new TemperatureRecord(20, TemperatureUnit.CELSIUS))
            .build();

    private ThermooEnvironmentAttributes() {

    }
}