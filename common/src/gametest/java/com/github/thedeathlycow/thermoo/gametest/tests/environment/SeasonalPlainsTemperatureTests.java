/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class SeasonalPlainsTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void plains_fallback_temperature_is_spring(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 20.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.SPRING)
    public void plains_spring_temperature_is_20c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 20.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.SUMMER)
    public void plains_summer_temperature_is_30c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 30.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.AUTUMN)
    public void plains_autumn_temperature_is_20c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 20.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WINTER)
    public void plains_winter_temperature_is_10c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 10.0, temperature);

        helper.succeed();
    }
}