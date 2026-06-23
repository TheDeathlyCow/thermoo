package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class DesertDryHumidityTests {
    @GameTest(environment = EnvironmentTestHelper.DRY)
    public void desert_dry_fallback_humidity_is_spring(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.2, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_SPRING)
    public void desert_dry_spring_humidity_is_20pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.2, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_SUMMER)
    public void desert_dry_summer_humidity_is_20pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.2, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_AUTUMN)
    public void desert_dry_autumn_humidity_is_10pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.1, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY_WINTER)
    public void desert_dry_winter_humidity_is_10pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.1, humidity);

        helper.succeed();
    }
}