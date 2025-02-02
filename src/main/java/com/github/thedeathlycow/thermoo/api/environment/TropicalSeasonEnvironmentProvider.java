package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.Optional;

public class TropicalSeasonEnvironmentProvider extends SeasonalEnvironmentProvider {
    public TropicalSeasonEnvironmentProvider(EnvironmentProvider fallback, Map<ThermooSeason, EnvironmentProvider> seasons) {
        super(fallback, seasons);
    }

    @Override
    protected Optional<EnvironmentProvider> getForSeason(ThermooSeason season) {
        if (season.isTropical()) {
            return super.getForSeason(season);
        }
        return Optional.empty();
    }
}