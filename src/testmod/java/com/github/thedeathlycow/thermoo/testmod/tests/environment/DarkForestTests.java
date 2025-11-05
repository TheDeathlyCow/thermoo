package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class DarkForestTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_temperature_is_10c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 10.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_humidity_is_51pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.51, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_winter_temperature_is_0c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 0.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_winter_humidity_is_25pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_summer_temperature_is_replaced_with_35c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 35.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void dark_forest_summer_humidity_is_replaced_with_75pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.succeed();
    }
}