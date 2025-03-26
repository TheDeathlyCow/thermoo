package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class DesertDryTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.DRY)
    public void desert_dry_fallback_temperature_is_spring(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_SPRING)
    public void desert_dry_spring_temperature_is_30c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_SUMMER)
    public void desert_dry_summer_temperature_is_40c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 40.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_AUTUMN)
    public void desert_dry_autumn_temperature_is_30c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_WINTER)
    public void desert_dry_winter_temperature_is_20c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, temperature);

        context.complete();
    }
}