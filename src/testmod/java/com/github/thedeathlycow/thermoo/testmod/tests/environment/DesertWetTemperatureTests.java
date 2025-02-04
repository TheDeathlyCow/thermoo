package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class DesertWetTemperatureTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_fallback_temperature_is_summer(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 41.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_spring_temperature_is_31c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SPRING, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 31.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_summer_temperature_is_41c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 41.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_autumn_temperature_is_31c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.AUTUMN, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 31.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_wet_winter_temperature_is_21c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 21.0, temperature);

        context.complete();
    }
}