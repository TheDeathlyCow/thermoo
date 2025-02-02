package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.Optional;

public final class TemperateSeasonEnvironmentProvider extends SeasonalEnvironmentProvider {
    public TemperateSeasonEnvironmentProvider(RegistryEntryList<Biome> biomes, EnvironmentProvider fallback, Map<ThermooSeason, EnvironmentProvider> seasons) {
        super(biomes, fallback, seasons);
    }

    @Override
    protected Optional<EnvironmentProvider> getForSeason(ThermooSeason season) {
        if (season.isTropical()) {
            return Optional.empty();
        }
        return super.getForSeason(season);
    }
}