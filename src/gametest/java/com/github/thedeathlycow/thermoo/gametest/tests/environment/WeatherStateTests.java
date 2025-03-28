package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.test.TestEnvironmentDefinition;
import net.minecraft.test.TestEnvironments;
import net.minecraft.text.Text;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class WeatherStateTests {
    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER, maxTicks = 102)
    public void snowy_taiga_during_sunny_is_neg5c(TestContext context) {
        context.waitAndRun(100L, () -> {
            ServerWorld world = context.getWorld();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_TAIGA);
            EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);

            context.complete();
        });
    }

    @GameTest(environment = EnvironmentTestHelper.RAINY_WEATHER, maxTicks = 102)
    public void snowy_taiga_during_rainy_is_neg10c(TestContext context) {
        context.waitAndRun(100L, () -> {
            ServerWorld world = context.getWorld();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_TAIGA);
            EnvironmentTestHelper.assertTemperatureEquals(context, -10.0, temperature);

            context.complete();
        });
    }

    @GameTest(environment = EnvironmentTestHelper.THUNDER_WEATHER, maxTicks = 102)
    public void snowy_taiga_during_thunder_is_neg15c(TestContext context) {
        context.waitAndRun(100L, () -> {
            ServerWorld world = context.getWorld();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_TAIGA);
            EnvironmentTestHelper.assertTemperatureEquals(context, -15.0, temperature);

            context.complete();
        });
    }
}