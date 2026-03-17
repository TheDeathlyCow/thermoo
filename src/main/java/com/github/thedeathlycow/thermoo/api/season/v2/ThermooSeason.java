package com.github.thedeathlycow.thermoo.api.season.v2;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains common functionality for all thermoo season types.
 *
 * @see TemperateSeason
 * @see TropicalSeason
 */
@ApiStatus.NonExtendable
public sealed interface ThermooSeason extends StringRepresentable permits TemperateSeason, TropicalSeason {
    /**
     * Creates a season state that represents the start of this season.
     */
    ThermooSeasonState<? extends ThermooSeason> createState();
}