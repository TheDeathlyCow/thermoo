package com.github.thedeathlycow.thermoo.impl.season;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasonState;

public record SeasonStateImpl<S extends ThermooSeason>(S season, float progress) implements ThermooSeasonState<S> {

}