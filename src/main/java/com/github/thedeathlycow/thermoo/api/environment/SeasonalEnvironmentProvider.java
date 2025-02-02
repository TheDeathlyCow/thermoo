package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

public abstract class SeasonalEnvironmentProvider extends EnvironmentProvider {

    private final ThermooSeason fallbackSeason;
    private final Map<ThermooSeason, EnvironmentProvider> seasons;

    protected SeasonalEnvironmentProvider(
            ThermooSeason fallbackSeason,
            Map<ThermooSeason, EnvironmentProvider> seasons
    ) {
        this.fallbackSeason = fallbackSeason;
        this.seasons = seasons;
    }

    @Override
    public final Optional<TemperatureRecord> getTemperature(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        ThermooSeason season = ThermooSeason.getCurrentSeason(world).orElse(this.fallbackSeason);

        Optional<EnvironmentProvider> provider = this.getForSeason(season);
        return provider.isPresent() ? provider.get().getTemperature(world, pos, biome) : Optional.empty();
    }

    @Override
    public final OptionalDouble getRelativeHumidity(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        ThermooSeason season = ThermooSeason.getCurrentSeason(world).orElse(this.fallbackSeason);

        Optional<EnvironmentProvider> provider = this.getForSeason(season);
        return provider.isPresent() ? provider.get().getRelativeHumidity(world, pos, biome) : OptionalDouble.empty();
    }

    protected Optional<EnvironmentProvider> getForSeason(ThermooSeason season) {
        return Optional.ofNullable(this.seasons.get(season));
    }

    public final ThermooSeason fallbackSeason() {
        return this.fallbackSeason;
    }

    public final Map<ThermooSeason, EnvironmentProvider> seasons() {
        return this.seasons;
    }

    protected static MapCodec<Map<ThermooSeason, EnvironmentProvider>> createSeasonMapCodec() {
        return Codec.simpleMap(ThermooSeason.CODEC, EnvironmentProvider.PROVIDER_CODEC, StringIdentifiable.toKeyable(ThermooSeason.values()))
                .validate(seasonMap -> {
                    if (!seasonMap.keySet().isEmpty()) {
                        return DataResult.success(seasonMap);
                    } else {
                        return DataResult.error(() -> "No key season in " + seasonMap);
                    }
                });
    }
}