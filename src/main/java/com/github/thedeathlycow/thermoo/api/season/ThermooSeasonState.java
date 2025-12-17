package com.github.thedeathlycow.thermoo.api.season;

import com.github.thedeathlycow.thermoo.impl.season.SeasonStateImpl;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ThermooSeasonState<S extends ThermooSeason> {
    S season();

    float progress();

    static <S extends ThermooSeason> ThermooSeasonState<S> of(S season) {
        return of(season, 0f);
    }

    static <S extends ThermooSeason> ThermooSeasonState<S> of(S season, float progress) {
        return new SeasonStateImpl<>(season, progress);
    }
}