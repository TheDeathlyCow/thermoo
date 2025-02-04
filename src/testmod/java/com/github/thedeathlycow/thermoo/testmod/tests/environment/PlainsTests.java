package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class PlainsTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_fallback_temperature_is_spring(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), null, null);

        double plainsTemperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, plainsTemperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_spring_temperature_is_20c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), ThermooSeason.SPRING, null);

        double plainsTemperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, plainsTemperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_summer_temperature_is_30c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), ThermooSeason.SUMMER, null);

        double plainsTemperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, plainsTemperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_autumn_temperature_is_20c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), ThermooSeason.AUTUMN, null);

        double plainsTemperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, plainsTemperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void plains_winter_temperature_is_10c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(world.getServer(), ThermooSeason.WINTER, null);

        double plainsTemperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 10.0, plainsTemperature);

        context.complete();
    }
}