package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class JungleHumidityTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void jungle_fallback_humidity_is_normal_fallback(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(context, RelativeHumidityComponent.DEFAULT, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void jungle_wet_humidity_is_100pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(context, 1.0, humidity);

        context.succeed();
    }

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void jungle_dry_humidity_is_25pc(GameTestHelper context) {
        Level world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, Biomes.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.25, humidity);

        context.succeed();
    }
}