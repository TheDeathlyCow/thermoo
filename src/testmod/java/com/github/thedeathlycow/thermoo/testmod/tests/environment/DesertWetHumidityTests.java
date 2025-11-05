package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class DesertWetHumidityTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_fallback_humidity_is_summer(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 1.0, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_spring_humidity_is_100pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SPRING, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 1.0, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_summer_humidity_is_100pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 1.0, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_autumn_humidity_is_80pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.AUTUMN, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.8, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_winter_humidity_is_80pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.8, humidity);

        context.succeed();
    }
}