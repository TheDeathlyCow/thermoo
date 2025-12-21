package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.gametest.GameTestHolder;

@SuppressWarnings("unused")
@GameTestHolder(ThermooTestMod.MODID)
public class PlainsTemperatureTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_fallback_temperature_is_spring(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_spring_temperature_is_20c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SPRING, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_summer_temperature_is_30c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_autumn_temperature_is_20c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.AUTUMN, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_winter_temperature_is_10c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 10.0, temperature);

        context.succeed();
    }
}