package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class SeasonalPlainsHumidityTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void plains_fallback_humidity_is_spring(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.75, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.SPRING)
    public void plains_spring_humidity_is_75pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.75, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.SUMMER)
    public void plains_summer_humidity_is_75pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.75, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.AUTUMN)
    public void plains_autumn_humidity_is_25pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.25, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WINTER)
    public void plains_winter_humidity_is_25pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, ThermooSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.25, humidity);

        helper.succeed();
    }
}