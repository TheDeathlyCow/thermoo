package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class PrecipitationTypeTests {

    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void snowy_plains_has_snowy_temperature_when_not_raining(TestContext context) {
        ServerWorld world = context.getWorld();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.RAINY_WEATHER, maxTicks = 102)
    public void snowy_plains_has_snowy_temperature_when_raining(TestContext context) {
        context.waitAndRun(100L, () -> {
            ServerWorld world = context.getWorld();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_PLAINS);
            EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);

            context.complete();
        });
    }

    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void taiga_has_rainy_temperature(TestContext context) {
        ServerWorld world = context.getWorld();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.TAIGA);
        EnvironmentTestHelper.assertTemperatureEquals(context, 15.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void end_barrens_has_none_temperature(TestContext context) {
        ServerWorld world = context.getWorld();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.END_BARRENS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 25.0, temperature);

        context.complete();
    }
}