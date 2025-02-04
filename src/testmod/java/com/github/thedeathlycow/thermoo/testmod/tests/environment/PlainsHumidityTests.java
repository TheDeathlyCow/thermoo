package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class PlainsHumidityTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_fallback_humidity_is_spring(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), null, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_spring_humidity_is_75pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), ThermooSeason.SPRING, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_summer_humidity_is_75pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), ThermooSeason.SUMMER, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_autumn_humidity_is_25pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), ThermooSeason.AUTUMN, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_winter_humidity_is_25pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), ThermooSeason.WINTER, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.complete();
    }
}