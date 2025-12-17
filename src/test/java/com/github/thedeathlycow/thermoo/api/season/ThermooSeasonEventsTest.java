package com.github.thedeathlycow.thermoo.api.season;

import com.github.thedeathlycow.thermoo.ThermooTest;
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

        var currentSeason = TemperateSeason.getCurrentSeason(null, null);

        Assertions.assertFalse(currentSeason.isEmpty());
        Assertions.assertSame(season, currentSeason.get());
    }

    @ParameterizedTest
    @EnumSource(
            value = TropicalSeason.class,
            names = {"WET", "DRY"}
    )
    void tropicalSeason_getCurrentTropicalSeason_isNotEmpty(TropicalSeason season) {
        currentTropicalSeason = season;

        var currentTropicalSeason = TropicalSeason.getCurrentSeason(null, null);

        Assertions.assertFalse(currentTropicalSeason.isEmpty());
        Assertions.assertSame(season, currentTropicalSeason.get());
    }
}