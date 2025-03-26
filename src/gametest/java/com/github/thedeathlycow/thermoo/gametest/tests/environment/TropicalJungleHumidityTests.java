package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class TropicalJungleHumidityTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jungle_fallback_humidity_is_normal_fallback(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(context, RelativeHumidityComponent.DEFAULT, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET)
    public void jungle_wet_humidity_is_100pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(context, 1.0, humidity);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY)
    public void jungle_dry_humidity_is_25pc(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.complete();
    }
}