package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class ConstantForestTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void forest_temperature_is_21c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 21.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void forest_humidity_is_51pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.51, humidity);

        context.complete();
    }
}