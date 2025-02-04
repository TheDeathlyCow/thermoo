package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class BirchForestTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void birch_forest_temperature_is_mean_of_forest_and_birch(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.BIRCH_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 25.5, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void birch_forest_humidity_is_mean_of_forest_and_birch(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.BIRCH_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.755, humidity);

        context.complete();
    }
}