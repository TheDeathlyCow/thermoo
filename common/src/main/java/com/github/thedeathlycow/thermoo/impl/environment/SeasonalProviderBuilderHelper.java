/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class SeasonalProviderBuilderHelper<Season extends Enum<Season>> {
    @Nullable
    private Season fallbackSeason = null;

    private final Map<Season, Holder<EnvironmentProvider>> seasons;

    public SeasonalProviderBuilderHelper(Class<Season> seasonClass) {
        this.seasons = new EnumMap<>(seasonClass);
    }

    @Nullable
    public Season getFallbackSeason() {
        return fallbackSeason;
    }

    public Map<Season, Holder<EnvironmentProvider>> getSeasons() {
        return seasons;
    }

    public void setFallbackSeason(@NotNull Season season) {
        Objects.requireNonNull(season);
        this.fallbackSeason = season;
    }

    public void setSeasonProvider(@NotNull Season season, @NotNull Holder<EnvironmentProvider> provider) {
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