package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class DesertDryHumidityTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_fallback_humidity_is_spring(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.2, humidity);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_spring_humidity_is_75pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SPRING, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.2, humidity);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_summer_humidity_is_75pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.2, humidity);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_autumn_humidity_is_25pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.AUTUMN, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.1, humidity);

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_winter_humidity_is_25pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.1, humidity);

        context.complete();
    }
}