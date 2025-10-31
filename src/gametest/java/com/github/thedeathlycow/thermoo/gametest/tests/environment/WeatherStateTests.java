package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class WeatherStateTests {
    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER, maxTicks = 102)
    public void snowy_taiga_during_sunny_is_neg5c(GameTestHelper helper) {
        helper.runAfterDelay(100L, () -> {
            ServerLevel level = helper.getLevel();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_TAIGA);
            EnvironmentTestHelper.assertTemperatureEquals(helper, -5.0, temperature);

            helper.succeed();
        });
    }

    @GameTest(environment = EnvironmentTestHelper.RAINY_WEATHER, maxTicks = 102)
    public void snowy_taiga_during_rainy_is_neg10c(GameTestHelper helper) {
        helper.runAfterDelay(100L, () -> {
            ServerLevel level = helper.getLevel();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_TAIGA);
            EnvironmentTestHelper.assertTemperatureEquals(helper, -10.0, temperature);

            helper.succeed();
        });
    }

    @GameTest(environment = EnvironmentTestHelper.THUNDER_WEATHER, maxTicks = 102)
    public void snowy_taiga_during_thunder_is_neg15c(GameTestHelper helper) {
        helper.runAfterDelay(100L, () -> {
            ServerLevel level = helper.getLevel();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_TAIGA);
            EnvironmentTestHelper.assertTemperatureEquals(helper, -15.0, temperature);

            helper.succeed();
        });
    }
}