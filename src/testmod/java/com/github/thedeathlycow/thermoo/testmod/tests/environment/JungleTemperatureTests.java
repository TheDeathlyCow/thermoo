package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class JungleTemperatureTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void jungle_fallback_temperature_is_normal_fallback(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.JUNGLE);
        EnvironmentTestHelper.assertTemperatureEquals(
                context,
                TemperatureRecordComponent.DEFAULT.value(),
                temperature
        );

        context.succeed();
    }
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void jungle_wet_temperature_is_30c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.JUNGLE);
        EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void jungle_dry_temperature_is_50c(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_DRY);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.JUNGLE);
        EnvironmentTestHelper.assertTemperatureEquals(context, 50.0, temperature);

        context.succeed();
    }
}