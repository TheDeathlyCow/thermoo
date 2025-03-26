package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class WeatherStateTests {
    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void snowy_taiga_during_sunny_is_neg5c(TestContext context) {
        ServerWorld world = context.getWorld();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_TAIGA);
        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.RAINY_WEATHER)
    public void snowy_taiga_during_rainy_is_neg10c(TestContext context) {
        ServerWorld world = context.getWorld();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_TAIGA);
        EnvironmentTestHelper.assertTemperatureEquals(context, -10.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.THUNDER_WEATHER)
    public void snowy_taiga_during_thunder_is_neg15c(TestContext context) {
        ServerWorld world = context.getWorld();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_TAIGA);
        EnvironmentTestHelper.assertTemperatureEquals(context, -15.0, temperature);

        context.complete();
    }
}