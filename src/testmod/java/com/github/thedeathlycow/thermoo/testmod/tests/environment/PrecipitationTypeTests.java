package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.AfterBatch;
import net.minecraft.test.BeforeBatch;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class PrecipitationTypeTests {
    @BeforeBatch(batchId = "rainy")
    public void setRainyForRainy(ServerWorld world) {
        world.setWeather(0, 100000, true, false);
    }

    @AfterBatch(batchId = "rainy")
    public void setClearForRainy(ServerWorld world) {
        world.setWeather(100000, 0, false, false);
    }

    @BeforeBatch(batchId = "sunny")
    public void setClearForSunny(ServerWorld world) {
        world.setWeather(100000, 0, false, false);
    }

    @GameTest(
            templateName = FabricGameTest.EMPTY_STRUCTURE,
            batchId = "rainy"
    )
    public void snowy_plains_has_snowy_temperature_when_not_raining(TestContext context) {
        ServerWorld world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);

        context.complete();
    }

    @GameTest(
            templateName = FabricGameTest.EMPTY_STRUCTURE,
            batchId = "sunny"
    )
    public void snowy_plains_has_snowy_temperature_when_raining(TestContext context) {
        ServerWorld world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);

        context.complete();
    }
}