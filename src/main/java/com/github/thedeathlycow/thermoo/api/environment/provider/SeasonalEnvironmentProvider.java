package com.github.thedeathlycow.thermoo.api.environment.provider;

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

/**
 * An environment provider that dispatches to another provider based on the current season state of a world.
 */
public abstract sealed class SeasonalEnvironmentProvider implements EnvironmentProvider
        permits TemperateSeasonEnvironmentProvider, TropicalSeasonEnvironmentProvider {

    private final Optional<ThermooSeason> fallbackSeason;
    private final Map<ThermooSeason, EnvironmentProvider> seasons;

    protected SeasonalEnvironmentProvider(
            Optional<ThermooSeason> fallbackSeason,
            Map<ThermooSeason, EnvironmentProvider> seasons
    ) {
        this.fallbackSeason = fallbackSeason;
        this.seasons = seasons;
    }

    /**
     * Gets the temperature for the position based on the world's current season state, using the
     * {@linkplain ThermooSeason season API}. If no seasons mod is installed, will return the value provided by the
     * {@linkplain #fallbackSeason fallback season}. If there is no fallback season, then returns empty.
     *
     * @param world The world/level being queried
     * @param pos   The position in the world to query
     * @param biome The biome at the position in the world
     * @return Returns the potential temperature record of the world and position.
     */
    @Override
    public final Optional<TemperatureRecord> getTemperature(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        Optional<ThermooSeason> season = ThermooSeason.getCurrentSeason(world).or(this::fallbackSeason);
        if (season.isEmpty()) {
            return Optional.empty();
        }

        Optional<EnvironmentProvider> provider = this.getForSeason(season.get());
        return provider.isPresent() ? provider.get().getTemperature(world, pos, biome) : Optional.empty();
    }

    /**
     * Gets the relative humidity for the position based on the world's current season state, using the
     * {@linkplain ThermooSeason season API}. If no seasons mod is installed, will return the value provided by the
     * {@linkplain #fallbackSeason fallback season}. If there is no fallback season, then returns empty.
     *
     * @param world The world/level being queried
     * @param pos   The position in the world to query
     * @param biome The biome at the position in the world
     * @return Returns the potential relative humidity of the world and position.
     */
    @Override
    public final OptionalDouble getRelativeHumidity(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        Optional<ThermooSeason> season = ThermooSeason.getCurrentSeason(world).or(this::fallbackSeason);
        if (season.isEmpty()) {
            return OptionalDouble.empty();
        }

        Optional<EnvironmentProvider> provider = this.getForSeason(season.get());
        return provider.isPresent() ? provider.get().getRelativeHumidity(world, pos, biome) : OptionalDouble.empty();
    }

    public final Optional<ThermooSeason> fallbackSeason() {
        return this.fallbackSeason;
    }

    public final Map<ThermooSeason, EnvironmentProvider> seasons() {
        return this.seasons;
    }

    protected Optional<EnvironmentProvider> getForSeason(ThermooSeason season) {
        return Optional.ofNullable(this.seasons.get(season));
    }

    protected static MapCodec<Map<ThermooSeason, EnvironmentProvider>> createSeasonMapCodec() {
        return Codec.simpleMap(ThermooSeason.CODEC, EnvironmentProvider.PROVIDER_CODEC, StringIdentifiable.toKeyable(ThermooSeason.values()))
                .validate(seasonMap -> {
                    if (!seasonMap.keySet().isEmpty()) {
                        return DataResult.success(seasonMap);
                    } else {
                        return DataResult.error(() -> "No season key in: " + seasonMap);
                    }
                });
    }

    protected static <T extends SeasonalEnvironmentProvider> MapCodec<T> validate(MapCodec<T> codec) {
        return codec
                .validate(
                        provider -> {
                            Optional<ThermooSeason> season = provider.fallbackSeason();
                            if (season.isEmpty()) {
                                return DataResult.success(provider);
                            } else if (!provider.seasons().containsKey(season.get())) {
                                return DataResult.error(() -> "Fallback season '" + season.get().asString() + "' is not a key in: " + provider.seasons());
                            } else {
                                return DataResult.success(provider);
                            }
                        }
                );
    }
}