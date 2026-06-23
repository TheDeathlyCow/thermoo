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

import com.github.thedeathlycow.thermoo.gametest.util.CustomTestMethodInvoker;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class ReferenceConstantBirchForestTests implements CustomTestMethodInvoker {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void birch_forest_temperature_is_22c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.BIRCH_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 22.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void birch_forest_humidity_is_52pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.BIRCH_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.52, humidity);

        helper.succeed();
    }

    @Override
    public void invokeTestMethod(GameTestHelper helper, Method method) throws ReflectiveOperationException {
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);
        method.invoke(this, helper);
    }
}