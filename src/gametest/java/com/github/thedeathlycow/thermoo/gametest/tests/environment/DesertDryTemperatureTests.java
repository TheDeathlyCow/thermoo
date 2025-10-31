package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class DesertDryTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.DRY)
    public void desert_dry_fallback_temperature_is_spring(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 30.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_SPRING)
    public void desert_dry_spring_temperature_is_30c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 30.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_SUMMER)
    public void desert_dry_summer_temperature_is_40c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 40.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_AUTUMN)
    public void desert_dry_autumn_temperature_is_30c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 30.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_WINTER)
    public void desert_dry_winter_temperature_is_20c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 20.0, temperature);

        helper.succeed();
    }
}