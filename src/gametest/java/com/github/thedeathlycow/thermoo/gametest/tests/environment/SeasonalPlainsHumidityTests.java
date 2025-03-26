package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class SeasonalPlainsHumidityTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void plains_fallback_humidity_is_spring(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.SPRING)
    public void plains_spring_humidity_is_75pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.SUMMER)
    public void plains_summer_humidity_is_75pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.75, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.AUTUMN)
    public void plains_autumn_humidity_is_25pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WINTER)
    public void plains_winter_humidity_is_25pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.complete();
    }
}