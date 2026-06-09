package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class PrecipitationTypeTests {

    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void snowy_plains_has_snowy_temperature_when_not_raining(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, -5.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.RAINY_WEATHER, maxTicks = 102)
    public void snowy_plains_has_snowy_temperature_when_raining(GameTestHelper helper) {
        helper.runAfterDelay(100L, () -> {
            ServerLevel level = helper.getLevel();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_PLAINS);
            EnvironmentTestHelper.assertTemperatureEquals(helper, -5.0, temperature);

            helper.succeed();
        });
    }

    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void taiga_has_rainy_temperature(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.TAIGA);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 15.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void end_barrens_has_none_temperature(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.END_BARRENS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 25.0, temperature);

        helper.succeed();
    }
}