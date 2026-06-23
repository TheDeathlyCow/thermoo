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

import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class PrecipitationTypeTests {

    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void snowy_plains_has_snowy_temperature_when_not_raining(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, -5.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.RAINY_WEATHER, maxTicks = 102)
    public void snowy_plains_has_snowy_temperature_when_raining(GameTestHelper helper) {
        helper.runAfterDelay(100L, () -> {
            ServerLevel level = helper.getLevel();

            double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.SNOWY_PLAINS);
            EnvironmentTestHelper.assertTemperatureEquals(helper, -5.0, temperature);

            helper.succeed();
        });
    }

    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void taiga_has_rainy_temperature(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.TAIGA);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 15.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.CLEAR_WEATHER)
    public void end_barrens_has_none_temperature(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.END_BARRENS);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 25.0, temperature);

        helper.succeed();
    }
}