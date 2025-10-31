package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class DesertWetTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.WET)
    public void desert_wet_fallback_temperature_is_summer(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 41.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SPRING)
    public void desert_wet_spring_temperature_is_31c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 31.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SUMMER)
    public void desert_wet_summer_temperature_is_41c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 41.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_AUTUMN)
    public void desert_wet_autumn_temperature_is_31c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 31.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_WINTER)
    public void desert_wet_winter_temperature_is_21c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 21.0, temperature);

        helper.succeed();
    }
}