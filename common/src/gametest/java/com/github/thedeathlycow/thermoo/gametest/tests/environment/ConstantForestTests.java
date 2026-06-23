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

import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class ConstantForestTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void forest_temperature_is_21c(GameTestHelper context) {
        ServerLevel level = context.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, level, Biomes.FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 21.0, temperature);

        context.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void forest_humidity_is_51pc(GameTestHelper context) {
        ServerLevel level = context.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, level, Biomes.FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.51, humidity);

        context.succeed();
    }
}