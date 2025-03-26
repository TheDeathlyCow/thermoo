package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class SeasonalPlainsTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void plains_fallback_temperature_is_spring(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.SPRING)
    public void plains_spring_temperature_is_20c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.SUMMER)
    public void plains_summer_temperature_is_30c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.AUTUMN)
    public void plains_autumn_temperature_is_20c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WINTER)
    public void plains_winter_temperature_is_10c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, 10.0, temperature);

        context.complete();
    }
}