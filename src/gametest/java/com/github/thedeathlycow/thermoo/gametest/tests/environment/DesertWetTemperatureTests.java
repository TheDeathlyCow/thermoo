package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class DesertWetTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.WET)
    public void desert_wet_fallback_temperature_is_summer(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 41.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SPRING)
    public void desert_wet_spring_temperature_is_31c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 31.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SUMMER)
    public void desert_wet_summer_temperature_is_41c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 41.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_AUTUMN)
    public void desert_wet_autumn_temperature_is_31c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 31.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_WINTER)
    public void desert_wet_winter_temperature_is_21c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 21.0, temperature);

        context.complete();
    }
}