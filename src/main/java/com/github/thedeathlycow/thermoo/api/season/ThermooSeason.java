package com.github.thedeathlycow.thermoo.api.season;

import net.minecraft.util.StringRepresentable;

/**
 * Contains common functionality for all thermoo season types.
 *
 * @see TemperateSeason
 * @see TropicalSeason
 */
public sealed interface ThermooSeason<S extends ThermooSeason<S>> extends StringRepresentable permits TemperateSeason, TropicalSeason {
    /**
     * Creates a season state that represents the start of this season.
     */
    ThermooSeasonState<S> createState();
}