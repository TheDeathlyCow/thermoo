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

package com.github.thedeathlycow.thermoo.api.season.v2;

import com.github.thedeathlycow.thermoo.impl.season.SeasonStateImpl;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains the state of the season at some particular time.
 *
 * @param <S> The season type, either temperate or tropical.
 */
@ApiStatus.NonExtendable
public interface ThermooSeasonState<S extends ThermooSeason> {
    /**
     * @return The season of this state.
     */
    S season();

    /**
     * How far this season has progressed. Note that some season mods may not implement this functionality, in which
     * case the progress will always be 0.
     *
     * @return A float from 0-1 representing the percentage of the season that has already elapsed.
     */
    float progress();

    /**
     * Creates a season state that represents the beginning of the season.
     */
    static <S extends ThermooSeason> ThermooSeasonState<S> of(S season) {
        return of(season, 0f);
    }

    /**
     * Creates a season state for some progress in a season. The progress is clamped to the range [0, 1].
     */
    static <S extends ThermooSeason> ThermooSeasonState<S> of(S season, float progress) {
        float clampedProgress = Mth.clamp(progress, 0f, 1f);

        return new SeasonStateImpl<>(season, clampedProgress);
    }

    /**
     * Creates a codec for a season state of a particular season type.
     *
     * @param seasonCodec The base codec of the season type.
     * @param <S>         The season type.
     * @return A new codec for the season state.
     */
    static <S extends ThermooSeason> Codec<ThermooSeasonState<S>> codec(Codec<S> seasonCodec) {
        return RecordCodecBuilder.create(
                instance -> instance.group(
                        seasonCodec
                                .fieldOf("season")
                                .forGetter(ThermooSeasonState::season),
                        Codec.floatRange(0f, 1f)
                                .optionalFieldOf("progress", 0f)
                                .forGetter(ThermooSeasonState::progress)
                ).apply(instance, ThermooSeasonState::of)
        );
    }
}