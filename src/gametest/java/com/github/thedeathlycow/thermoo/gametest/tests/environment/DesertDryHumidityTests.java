package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class DesertDryHumidityTests {
    @GameTest(environment = EnvironmentTestHelper.DRY)
    public void desert_dry_fallback_humidity_is_spring(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.2, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_SPRING)
    public void desert_dry_spring_humidity_is_20pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.2, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_SUMMER)
    public void desert_dry_summer_humidity_is_20pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.2, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_AUTUMN)
    public void desert_dry_autumn_humidity_is_10pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.1, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_WINTER)
    public void desert_dry_winter_humidity_is_10pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.1, humidity);

        context.complete();
    }
}