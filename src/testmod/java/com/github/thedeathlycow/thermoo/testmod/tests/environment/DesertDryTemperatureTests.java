package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class DesertDryTemperatureTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_fallback_temperature_is_spring(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_spring_temperature_is_30c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SPRING, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_summer_temperature_is_40c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 40.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_autumn_temperature_is_30c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.AUTUMN, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_winter_temperature_is_20c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, temperature);

        context.complete();
    }
}