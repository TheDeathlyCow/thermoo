package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class ModifiedDarkForestTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void dark_forest_temperature_is_10c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 10.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void dark_forest_humidity_is_51pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.51, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WINTER)
    public void dark_forest_winter_temperature_is_0c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 0.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WINTER)
    public void dark_forest_winter_humidity_is_25pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.SUMMER)
    public void dark_forest_summer_temperature_is_replaced_with_35c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 35.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.SUMMER)
    public void dark_forest_summer_humidity_is_replaced_with_75pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DARK_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.complete();
    }
}