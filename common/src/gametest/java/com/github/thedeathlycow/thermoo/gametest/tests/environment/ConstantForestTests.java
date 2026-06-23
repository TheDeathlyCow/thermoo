package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class ConstantForestTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void forest_temperature_is_21c(GameTestHelper context) {
        ServerLevel level = context.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, level, Biomes.FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 21.0, temperature);

        context.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void forest_humidity_is_51pc(GameTestHelper context) {
        ServerLevel level = context.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, level, Biomes.FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.51, humidity);

        context.succeed();
    }
}