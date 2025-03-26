package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class DesertWetHumidityTests {
    @GameTest(environment = EnvironmentTestHelper.WET)
    public void desert_wet_fallback_humidity_is_summer(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 1.0, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SPRING)
    public void desert_wet_spring_humidity_is_100pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 1.0, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SUMMER)
    public void desert_wet_summer_humidity_is_100pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 1.0, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_AUTUMN)
    public void desert_wet_autumn_humidity_is_80pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.8, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_WINTER)
    public void desert_wet_winter_humidity_is_80pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.8, humidity);

        context.complete();
    }
}