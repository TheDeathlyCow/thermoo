package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.Optional;

public abstract class SeasonalEnvironmentProvider extends EnvironmentProvider {

    private final EnvironmentProvider fallback;
    private final Map<ThermooSeason, EnvironmentProvider> seasons;

    protected SeasonalEnvironmentProvider(
            EnvironmentProvider fallback,
            Map<ThermooSeason, EnvironmentProvider> seasons
    ) {
        this.fallback = fallback;
        this.seasons = seasons;
    }

    @Override
    public final TemperatureRecord getTemperature(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        Optional<ThermooSeason> season = ThermooSeason.getCurrentSeason(world);
        if (season.isEmpty()) {
            return this.fallback.getTemperature(world, pos, biome);
        }

        Optional<EnvironmentProvider> provider = this.getForSeason(season.get());
        return provider.map(environmentProvider -> environmentProvider.getTemperature(world, pos, biome))
                .orElseGet(() -> this.fallback.getTemperature(world, pos, biome));
    }

    @Override
    public final double getRelativeHumidity(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        Optional<ThermooSeason> season = ThermooSeason.getCurrentSeason(world);
        if (season.isEmpty()) {
            return this.fallback.getRelativeHumidity(world, pos, biome);
        }

        Optional<EnvironmentProvider> provider = this.getForSeason(season.get());
        return provider.map(environmentProvider -> environmentProvider.getRelativeHumidity(world, pos, biome))
                .orElse(this.fallback.getRelativeHumidity(world, pos, biome));
    }

    protected Optional<EnvironmentProvider> getForSeason(ThermooSeason season) {
        return Optional.ofNullable(this.seasons.get(season));
    }

    public EnvironmentProvider fallback() {
        return fallback;
    }

    public Map<ThermooSeason, EnvironmentProvider> seasons() {
        return seasons;
    }
}