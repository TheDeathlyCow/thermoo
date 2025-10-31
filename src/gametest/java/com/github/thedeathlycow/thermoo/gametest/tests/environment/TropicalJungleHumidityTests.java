package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class TropicalJungleHumidityTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jungle_fallback_humidity_is_normal_fallback(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(helper, RelativeHumidityComponent.DEFAULT, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET)
    public void jungle_wet_humidity_is_100pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(helper, 1.0, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY)
    public void jungle_dry_humidity_is_25pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, ThermooSeason.TROPICAL_DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.25, humidity);

        helper.succeed();
    }
}