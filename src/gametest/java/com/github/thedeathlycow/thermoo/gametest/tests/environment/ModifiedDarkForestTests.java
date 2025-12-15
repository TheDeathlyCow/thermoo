package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class ModifiedDarkForestTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void dark_forest_temperature_is_10c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 10.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void dark_forest_humidity_is_51pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.51, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WINTER)
    public void dark_forest_winter_temperature_is_0c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 0.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WINTER)
    public void dark_forest_winter_humidity_is_25pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.25, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.SUMMER)
    public void dark_forest_summer_temperature_is_replaced_with_35c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 35.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.SUMMER)
    public void dark_forest_summer_humidity_is_replaced_with_75pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.75, humidity);

        helper.succeed();
    }
}