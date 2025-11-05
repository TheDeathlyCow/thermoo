package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class ForestTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void forest_temperature_is_21c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 21.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void forest_humidity_is_51pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.51, humidity);

        context.succeed();
    }
}