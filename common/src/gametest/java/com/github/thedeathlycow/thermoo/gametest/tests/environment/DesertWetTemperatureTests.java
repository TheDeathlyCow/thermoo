/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
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
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class DesertWetTemperatureTests {
    @GameTest(environment = EnvironmentTestHelper.WET)
    public void desert_wet_fallback_temperature_is_summer(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 41.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SPRING)
    public void desert_wet_spring_temperature_is_31c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SPRING);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 31.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_SUMMER)
    public void desert_wet_summer_temperature_is_41c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.SUMMER);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 41.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_AUTUMN)
    public void desert_wet_autumn_temperature_is_31c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.AUTUMN);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 31.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET_WINTER)
    public void desert_wet_winter_temperature_is_21c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, TemperateSeason.WINTER);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.DESERT);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 21.0, temperature);

        helper.succeed();
    }
}