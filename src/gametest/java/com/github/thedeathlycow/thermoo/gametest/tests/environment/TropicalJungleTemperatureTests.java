package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("unused")
public class TropicalJungleTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jungle_fallback_temperature_is_normal_fallback(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.JUNGLE);
        EnvironmentTestHelper.assertTemperatureEquals(
                context,
                TemperatureRecordComponent.DEFAULT.value(),
                temperature
        );

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.WET)
    public void jungle_wet_temperature_is_30c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.JUNGLE);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.complete();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY)
    public void jungle_dry_temperature_is_50c(TestContext context) {
        World world = context.getWorld();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.JUNGLE);
        EnvironmentTestHelper.assertTemperatureEquals(context, 50.0, temperature);

        context.complete();
    }
}