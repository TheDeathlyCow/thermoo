package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

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

    public Optional<RegistryEntry<EnvironmentProvider>> clear() {
        return clear;
    }

    public Optional<RegistryEntry<EnvironmentProvider>> rain() {
        return rain;
    }

    public Optional<RegistryEntry<EnvironmentProvider>> thunder() {
        return thunder;
    }
}