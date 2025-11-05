package com.github.thedeathlycow.thermoo.api.season;

import ThermooSeason;
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
    static ThermooSeason currentSeason = null;

    @Nullable
    static ThermooSeason currentTropicalSeason = null;

    @BeforeAll
    static void setup() {
        ThermooTest.bootstrapRegistries();
        ThermooSeasonEvents.GET_CURRENT_SEASON.register(world -> Optional.ofNullable(currentSeason));
        ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON.register((world, pos) -> Optional.ofNullable(currentTropicalSeason));
    }

    @AfterEach
    void resetSeasons() {
        currentSeason = null;
        currentTropicalSeason = null;
    }

    @ParameterizedTest
    @EnumSource(
            value = ThermooSeason.class,
            names = {"SPRING", "SUMMER", "AUTUMN", "WINTER"}
    )
    void temperateSeason_getCurrentSeason_isNotEmpty(ThermooSeason season) {
        currentSeason = season;

        Optional<ThermooSeason> currentSeason = ThermooSeason.getCurrentSeason(null);

        Assertions.assertFalse(currentSeason.isEmpty());
        Assertions.assertSame(season, currentSeason.get());
    }

    @ParameterizedTest
    @EnumSource(
            value = ThermooSeason.class,
            names = {"TROPICAL_WET", "TROPICAL_DRY"}
    )
    void tropicalSeason_getCurrentSeason_isEmpty(ThermooSeason season) {
        currentSeason = season;

        Optional<ThermooSeason> currentSeason = ThermooSeason.getCurrentSeason(null);

        Assertions.assertTrue(currentSeason.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(
            value = ThermooSeason.class,
            names = {"SPRING", "SUMMER", "AUTUMN", "WINTER"}
    )
    void temperateSeason_getCurrentTropicalSeason_isEmpty(ThermooSeason season) {
        currentTropicalSeason = season;

        Optional<ThermooSeason> currentTropicalSeason = ThermooSeason.getCurrentTropicalSeason(null, null);

        Assertions.assertTrue(currentTropicalSeason.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(
            value = ThermooSeason.class,
            names = {"TROPICAL_WET", "TROPICAL_DRY"}
    )
    void tropicalSeason_getCurrentTropicalSeason_isNotEmpty(ThermooSeason season) {
        currentTropicalSeason = season;

        Optional<ThermooSeason> currentTropicalSeason = ThermooSeason.getCurrentTropicalSeason(null, null);

        Assertions.assertFalse(currentTropicalSeason.isEmpty());
        Assertions.assertSame(season, currentTropicalSeason.get());
    }
}