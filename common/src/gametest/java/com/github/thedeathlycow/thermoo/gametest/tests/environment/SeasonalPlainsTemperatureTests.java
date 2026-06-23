package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class SeasonalPlainsTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void plains_fallback_temperature_is_spring(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 20.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.SPRING)
    public void plains_spring_temperature_is_20c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 20.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.SUMMER)
    public void plains_summer_temperature_is_30c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 30.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.AUTUMN)
    public void plains_autumn_temperature_is_20c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 20.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WINTER)
    public void plains_winter_temperature_is_10c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 10.0, temperature);

        helper.succeed();
    }
}