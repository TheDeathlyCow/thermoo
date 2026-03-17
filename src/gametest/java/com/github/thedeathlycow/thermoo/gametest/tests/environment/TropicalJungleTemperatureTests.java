package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.v2.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class TropicalJungleTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jungle_fallback_temperature_is_normal_fallback(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.JUNGLE);
        EnvironmentTestHelper.assertTemperatureEquals(
                helper,
                TemperatureRecordComponent.DEFAULT.value(),
                temperature
        );

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET)
    public void jungle_wet_temperature_is_30c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.JUNGLE);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 30.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY)
    public void jungle_dry_temperature_is_50c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.JUNGLE);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 50.0, temperature);

        helper.succeed();
    }
}