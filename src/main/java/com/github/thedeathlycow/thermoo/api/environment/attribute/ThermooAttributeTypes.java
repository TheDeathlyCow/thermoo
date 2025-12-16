package com.github.thedeathlycow.thermoo.api.environment.attribute;

import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import net.minecraft.world.attribute.AttributeType;

import java.util.Optional;

public final class ThermooAttributeTypes {
    public static final AttributeType<Optional<TemperateSeason>> TEMPERATE_SEASON = AttributeType.ofNotInterpolated(
            TemperateSeason.CODEC.optionalFieldOf("value").codec()
    );

    public static final AttributeType<Optional<TropicalSeason>> TROPICAL_SEASON = AttributeType.ofNotInterpolated(
            TropicalSeason.CODEC.optionalFieldOf("value").codec()
    );

    private ThermooAttributeTypes() {

    }
}