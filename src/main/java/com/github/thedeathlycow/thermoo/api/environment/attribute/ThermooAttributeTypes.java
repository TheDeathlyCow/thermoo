package com.github.thedeathlycow.thermoo.api.environment.attribute;

import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AttributeType;

import java.util.Optional;

public final class ThermooAttributeTypes {
    public static final AttributeType<Optional<TemperateSeason>> TEMPERATE_SEASON = register(
            "temperate_season",
            AttributeType.ofNotInterpolated(TemperateSeason.CODEC.optionalFieldOf("value").codec())
    );

    public static final AttributeType<Optional<TropicalSeason>> TROPICAL_SEASON = register(
            "tropical_season",
            AttributeType.ofNotInterpolated(TropicalSeason.CODEC.optionalFieldOf("value").codec())
    );

    private static <V> AttributeType<V> register(String name, AttributeType<V> type) {
        return Registry.register(BuiltInRegistries.ATTRIBUTE_TYPE, Thermoo.id(name), type);
    }

    private ThermooAttributeTypes() {

    }
}