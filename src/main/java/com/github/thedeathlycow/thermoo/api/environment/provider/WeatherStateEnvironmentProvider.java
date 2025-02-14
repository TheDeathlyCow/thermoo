package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

/**
 * An environment provider that delegates to a child provider based on the global weather state of a world (clear, rain,
 * or thunder).
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class WeatherStateEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<WeatherStateEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnvironmentProvider.ENTRY_CODEC
                            .optionalFieldOf("clear")
                            .forGetter(WeatherStateEnvironmentProvider::clear),
                    EnvironmentProvider.ENTRY_CODEC
                            .optionalFieldOf("rain")
                            .forGetter(WeatherStateEnvironmentProvider::rain),
                    EnvironmentProvider.ENTRY_CODEC
                            .optionalFieldOf("thunder")
                            .forGetter(WeatherStateEnvironmentProvider::thunder)
            ).apply(instance, WeatherStateEnvironmentProvider::new)
    );

    private final Optional<RegistryEntry<EnvironmentProvider>> clear;
    private final Optional<RegistryEntry<EnvironmentProvider>> rain;
    private final Optional<RegistryEntry<EnvironmentProvider>> thunder;

    private WeatherStateEnvironmentProvider(
            Optional<RegistryEntry<EnvironmentProvider>> clear,
            Optional<RegistryEntry<EnvironmentProvider>> rain,
            Optional<RegistryEntry<EnvironmentProvider>> thunder
    ) {
        this.clear = clear;
        this.rain = rain;
        this.thunder = thunder;
    }

    /**
     * Delegates to a child provider based on the global weather state. If a provider is not defined for the current
     * weather state, then does nothing.
     *
     * @param world   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder A reducible component map builder to append to
     */
    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        if (world.isThundering()) {
            this.thunder.ifPresent(p -> p.value().buildCurrentComponents(world, pos, biome, builder));
        } else if (world.isRaining()) {
            this.rain.ifPresent(p -> p.value().buildCurrentComponents(world, pos, biome, builder));
        } else {
            this.clear.ifPresent(p -> p.value().buildCurrentComponents(world, pos, biome, builder));
        }
    }

    @Override
    public EnvironmentProviderType<WeatherStateEnvironmentProvider> getType() {
        return EnvironmentProviderTypes.WEATHER_STATE;
    }

    /**
     * Provider to use when the world is neither raining nor thundering
     */
    public Optional<RegistryEntry<EnvironmentProvider>> clear() {
        return clear;
    }

    /**
     * Provider to use when the world is raining but not thundering
     */
    public Optional<RegistryEntry<EnvironmentProvider>> rain() {
        return rain;
    }

    /**
     * Provider to use when the world is thundering
     */
    public Optional<RegistryEntry<EnvironmentProvider>> thunder() {
        return thunder;
    }
}