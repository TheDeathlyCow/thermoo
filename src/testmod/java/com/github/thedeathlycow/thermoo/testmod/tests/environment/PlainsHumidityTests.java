package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class PlainsHumidityTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_fallback_humidity_is_spring(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_spring_humidity_is_75pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SPRING, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_summer_humidity_is_75pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_autumn_humidity_is_25pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.AUTUMN, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_winter_humidity_is_25pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.succeed();
    }
}