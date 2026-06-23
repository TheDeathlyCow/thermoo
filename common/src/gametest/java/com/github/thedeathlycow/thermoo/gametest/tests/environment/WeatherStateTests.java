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
public class WeatherStateTests {
    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER, maxTicks = 102)
    public void snowy_taiga_during_sunny_is_neg5c(GameTestHelper helper) {
        helper.runAfterDelay(100L, () -> {
            ServerLevel level = helper.getLevel();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_TAIGA);
            EnvironmentTestHelper.assertTemperatureEquals(helper, -5.0, temperature);

            helper.succeed();
        });
    }

    @GameTest(environment = EnvironmentTestHelper.RAINY_WEATHER, maxTicks = 102)
    public void snowy_taiga_during_rainy_is_neg10c(GameTestHelper helper) {
        helper.runAfterDelay(100L, () -> {
            ServerLevel level = helper.getLevel();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_TAIGA);
            EnvironmentTestHelper.assertTemperatureEquals(helper, -10.0, temperature);

            helper.succeed();
        });
    }

    @GameTest(environment = EnvironmentTestHelper.THUNDER_WEATHER, maxTicks = 102)
    public void snowy_taiga_during_thunder_is_neg15c(GameTestHelper helper) {
        helper.runAfterDelay(100L, () -> {
            ServerLevel level = helper.getLevel();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_TAIGA);
            EnvironmentTestHelper.assertTemperatureEquals(helper, -15.0, temperature);

            helper.succeed();
        });
    }
}