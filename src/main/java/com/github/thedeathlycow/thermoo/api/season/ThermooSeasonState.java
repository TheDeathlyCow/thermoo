package com.github.thedeathlycow.thermoo.api.season;

import com.github.thedeathlycow.thermoo.impl.season.SeasonStateImpl;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains the state of the season at some particular time.
 *
 * @param <S> The season type, either temperate or tropical.
 */
@ApiStatus.NonExtendable
public interface ThermooSeasonState<S extends ThermooSeason<S>> {
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

    static <S extends ThermooSeason<S>> ThermooSeasonState<S> of(S season) {
        return of(season, 0f);
    }

    static <S extends ThermooSeason<S>> ThermooSeasonState<S> of(S season, float progress) {
        return new SeasonStateImpl<>(season, progress);
    }

    static <S extends ThermooSeason<S>> Codec<ThermooSeasonState<S>> codec(Codec<S> seasonCodec) {
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