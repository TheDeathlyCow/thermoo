package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class DesertDryTemperatureTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_fallback_temperature_is_spring(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_spring_temperature_is_30c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SPRING, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_summer_temperature_is_40c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 40.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_autumn_temperature_is_30c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.AUTUMN, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void desert_dry_winter_temperature_is_20c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 20.0, temperature);

        context.succeed();
    }
}