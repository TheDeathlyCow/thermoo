package com.github.thedeathlycow.thermoo.api.environment.attribute;

import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.EnvironmentAttribute;

import java.util.Optional;

public final class ThermooEnvironmentAttributes {
    public static final EnvironmentAttribute<Optional<TemperateSeason>> TEMPERATE_SEASON = register(
            "gameplay/temperate_season",
            EnvironmentAttribute.builder(ThermooAttributeTypes.TEMPERATE_SEASON)
    );

    public static final EnvironmentAttribute<Optional<TropicalSeason>> TROPICAL_SEASON = register(
            "gameplay/tropical_season",
            EnvironmentAttribute.builder(ThermooAttributeTypes.TROPICAL_SEASON)
    );

    private static <V> EnvironmentAttribute<V> register(String name, EnvironmentAttribute.Builder<V> builder) {
        return Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, Thermoo.id(name), builder.build());
    }

    private ThermooEnvironmentAttributes() {

    }
}