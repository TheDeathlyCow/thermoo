package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.gametest.GameTestHolder;

@SuppressWarnings("unused")
@GameTestHolder(ThermooTestMod.MODID)
public class BirchForestTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void birch_forest_temperature_is_22c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.BIRCH_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 22.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void birch_forest_humidity_is_52pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.BIRCH_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.52, humidity);

        context.succeed();
    }
}