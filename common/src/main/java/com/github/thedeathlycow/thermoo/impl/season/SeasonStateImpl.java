package com.github.thedeathlycow.thermoo.impl.season;

import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeasonState;

public record SeasonStateImpl<S extends ThermooSeason>(S season, float progress) implements ThermooSeasonState<S> {

}