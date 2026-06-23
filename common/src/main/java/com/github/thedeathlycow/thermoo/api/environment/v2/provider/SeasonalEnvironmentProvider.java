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

package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeasonEvents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * An environment provider that dispatches to another provider based on the current season state of a world.
 */
public abstract sealed class SeasonalEnvironmentProvider<S extends Enum<S> & StringRepresentable> implements EnvironmentProvider
        permits TemperateSeasonSelector, TropicalSeasonSelector {
    private final Optional<S> fallbackSeason;
    private final Map<S, Holder<EnvironmentProvider>> seasons;

    protected SeasonalEnvironmentProvider(
            Optional<S> fallbackSeason,
            Map<S, Holder<EnvironmentProvider>> seasons,
            Class<S> seasonClass
    ) {
        this.fallbackSeason = fallbackSeason;
        this.seasons = new EnumMap<>(seasonClass);
        this.seasons.putAll(seasons);
    }

    /**
     * Builds the environment components based on the world's current season state, generally using the
     * {@link ThermooSeasonEvents season API}.
     * <p>
     * If no seasons mod is installed, or if the tropical/temperate season state does not exist at this world position,
     * then this will use the components provided by the {@link #fallbackSeason fallback season}.
     * <p>
     * If there is no fallback season, then this does nothing.
     *
     * @param level   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder Component map builder to append to
     */
    @Override
    public final void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        Optional<S> season = this.getCurrentSeason(level, pos).or(this::fallbackSeason);
        if (season.isPresent()) {
            Holder<EnvironmentProvider> provider = this.seasons.get(season.get());
            if (provider != null) {
                provider.value().buildCurrentComponents(level, pos, biome, builder);
            }
        }
    }

    /**
     * The fallback season to use if no season mod is installed. If specified, the fallback season must be a key in the
     * {@link #seasons()} map. If no fallback season is provided, and there is no season mod installed, then this
     * provider will return nothing.
     *
     * @return Returns {@link #fallbackSeason}
     */
    public final Optional<S> fallbackSeason() {
        return this.fallbackSeason;
    }

    /**
     * The season-to-provider lookup back. Used to dispatch this provider to another provider based on the current season
     * of a world.
     *
     * @return Returns an unmodifiable map of {@link #seasons}
     */
    public final Map<S, Holder<EnvironmentProvider>> seasons() {
        return Collections.unmodifiableMap(this.seasons);
    }

    /**
     * Gets the current season state of the world at a position (usually by delegating to a
     * {@linkplain ThermooSeasonEvents season event}.
     *
     * @param level The world to query the season state of
     * @param pos   The position to query the season state at
     * @return Returns the season state of a particular world position, or empty if no season state exists there or if a
     * season mod is not loaded.
     */
    protected abstract Optional<S> getCurrentSeason(Level level, BlockPos pos);

    protected static <S extends Enum<S> & StringRepresentable> MapCodec<Map<S, Holder<EnvironmentProvider>>> createSeasonMapCodec(
            Codec<S> baseCodec,
            S[] values
    ) {
        return Codec.simpleMap(
                baseCodec,
                EnvironmentProvider.HOLDER_CODEC,
                StringRepresentable.keys(values)
        ).validate(seasonMap -> {
            if (seasonMap.isEmpty()) {
                return DataResult.error(() -> "No season key in: " + seasonMap);
            } else {
                return DataResult.success(seasonMap);
            }
        });
    }

    protected static <S extends Enum<S> & StringRepresentable, T extends SeasonalEnvironmentProvider<S>> MapCodec<T> validate(MapCodec<T> codec) {
        return codec
                .validate(
                        provider -> {
                            Optional<S> season = provider.fallbackSeason();
                            if (season.isEmpty()) {
                                return DataResult.success(provider);
                            } else if (!provider.seasons().containsKey(season.get())) {
                                return DataResult.error(() -> "Fallback season '" + season.get().getSerializedName() + "' is not a key in: " + provider.seasons());
                            } else {
                                return DataResult.success(provider);
                            }
                        }
                );
    }
}