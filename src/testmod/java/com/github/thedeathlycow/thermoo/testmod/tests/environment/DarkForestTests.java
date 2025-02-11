package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class DarkForestTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_temperature_is_10c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 10.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_humidity_is_51pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.51, humidity);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_winter_temperature_is_0c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 0.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_winter_humidity_is_25pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.complete();
    }
}