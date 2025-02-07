package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class SeasonalProviderBuilderHelper {
    @Nullable
    private ThermooSeason fallbackSeason = null;

    private final Map<ThermooSeason, RegistryEntry<EnvironmentProvider>> seasons = new EnumMap<>(ThermooSeason.class);

    @Nullable
    public ThermooSeason getFallbackSeason() {
        return fallbackSeason;
    }

    public Map<ThermooSeason, RegistryEntry<EnvironmentProvider>> getSeasons() {
        return seasons;
    }

    public void setFallbackSeason(@NotNull ThermooSeason season) {
        Objects.requireNonNull(season);
        this.fallbackSeason = season;
    }

    public void setSeasonProvider(@NotNull ThermooSeason season, @NotNull RegistryEntry<EnvironmentProvider> provider) {
        Objects.requireNonNull(season);
        Objects.requireNonNull(provider);
        this.seasons.put(season, provider);
    }

    public void validate() {
        if (this.seasons.isEmpty()) {
            throw new IllegalStateException("Cannot build a season provider with empty seasons map");
        }

        if (this.fallbackSeason != null && !this.seasons.containsKey(this.fallbackSeason)) {
            throw new IllegalStateException("Fallback season is not a key of season provider map");
        }
    }
}