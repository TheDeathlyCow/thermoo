package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class DesertWetHumidityTests {
    @GameTest(environment = EnvironmentTestHelper.WET)
    public void desert_wet_fallback_humidity_is_summer(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 1.0, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SPRING)
    public void desert_wet_spring_humidity_is_100pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 1.0, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SUMMER)
    public void desert_wet_summer_humidity_is_100pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 1.0, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_AUTUMN)
    public void desert_wet_autumn_humidity_is_80pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.8, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_WINTER)
    public void desert_wet_winter_humidity_is_80pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.8, humidity);

        helper.succeed();
    }
}