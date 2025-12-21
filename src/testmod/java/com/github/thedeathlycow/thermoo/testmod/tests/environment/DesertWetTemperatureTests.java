package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@SuppressWarnings("unused")
@GameTestHolder(ThermooTestMod.MODID)
@PrefixGameTestTemplate(false)
public class DesertWetTemperatureTests {
    @GameTest(template = ThermooTestMod.EMPTY_STRUCTURE)
    public void desert_wet_fallback_temperature_is_summer(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 41.0, temperature);

        context.succeed();
    }

    @GameTest(template = ThermooTestMod.EMPTY_STRUCTURE)
    public void desert_wet_spring_temperature_is_31c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SPRING, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 31.0, temperature);

        context.succeed();
    }

    @GameTest(template = ThermooTestMod.EMPTY_STRUCTURE)
    public void desert_wet_summer_temperature_is_41c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.SUMMER, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 41.0, temperature);

        context.succeed();
    }

    @GameTest(template = ThermooTestMod.EMPTY_STRUCTURE)
    public void desert_wet_autumn_temperature_is_31c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.AUTUMN, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 31.0, temperature);

        context.succeed();
    }

    @GameTest(template = ThermooTestMod.EMPTY_STRUCTURE)
    public void desert_wet_winter_temperature_is_21c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, ThermooSeason.WINTER, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(context, 21.0, temperature);

        context.succeed();
    }
}