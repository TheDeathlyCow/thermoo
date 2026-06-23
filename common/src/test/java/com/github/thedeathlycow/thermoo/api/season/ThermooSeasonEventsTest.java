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

package com.github.thedeathlycow.thermoo.api.season;

import com.github.thedeathlycow.thermoo.ThermooTest;
import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeasonEvents;
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;

class ThermooSeasonEventsTest {
    @Nullable
    static TemperateSeason currentSeason = null;

    @Nullable
    static TropicalSeason currentTropicalSeason = null;

    @BeforeAll
    static void setup() {
        ThermooTest.bootstrapRegistries();
        ThermooSeasonEvents.GET_CURRENT_SEASON.register((world, pos) -> Optional.ofNullable(currentSeason).map(TemperateSeason::createState));
        ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON.register((world, pos) -> Optional.ofNullable(currentTropicalSeason).map(TropicalSeason::createState));
    }

    @AfterEach
    void resetSeasons() {
        currentSeason = null;
        currentTropicalSeason = null;
    }

    @ParameterizedTest
    @EnumSource(
            value = TemperateSeason.class,
            names = {"SPRING", "SUMMER", "AUTUMN", "WINTER"}
    )
    void temperateSeason_getCurrentSeason_isNotEmpty(TemperateSeason season) {
        currentSeason = season;

        var returnedSeason = TemperateSeason.getCurrentState(null, null);

        Assertions.assertFalse(returnedSeason.isEmpty());
        Assertions.assertEquals(season, returnedSeason.get().season());
    }

    @ParameterizedTest
    @EnumSource(
            value = TropicalSeason.class,
            names = {"WET", "DRY"}
    )
    void tropicalSeason_getCurrentTropicalSeason_isNotEmpty(TropicalSeason season) {
        currentTropicalSeason = season;

        var returnedSeason = TropicalSeason.getCurrentState(null, null);

        Assertions.assertFalse(returnedSeason.isEmpty());
        Assertions.assertEquals(season, returnedSeason.get().season());
    }
}