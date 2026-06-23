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

import com.github.thedeathlycow.thermoo.api.environment.v2.component.AtmosphericPressureComponent;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class PressureTests  {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jagged_peaks_is_default_at_sea_level(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double pressure = EnvironmentTestHelper.getBiomePressure(helper, level, level.getSeaLevel(), Biomes.JAGGED_PEAKS);
        EnvironmentTestHelper.assertPressureEquals(helper, AtmosphericPressureComponent.DEFAULT, pressure);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jagged_peaks_is_low_pressure_at_max_altitude(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double pressure = EnvironmentTestHelper.getBiomePressure(helper, level, level.getMaxY(), Biomes.JAGGED_PEAKS);

        helper.assertTrue(
                pressure < AtmosphericPressureComponent.DEFAULT,
                Component.literal("Measured pressure " + pressure + " mbar was not less than " + AtmosphericPressureComponent.DEFAULT + " mbar")
        );

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jagged_peaks_is_high_pressure_at_min_altitude(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double pressure = EnvironmentTestHelper.getBiomePressure(helper, level, level.getMinY(), Biomes.JAGGED_PEAKS);

        helper.assertTrue(
                pressure > AtmosphericPressureComponent.DEFAULT,
                Component.literal("Measured pressure " + pressure + " mbar was not greater than " + AtmosphericPressureComponent.DEFAULT + " mbar")
        );

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jagged_peaks_is_constant_pressure_above_max_altitude(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        double pressure = EnvironmentTestHelper.getBiomePressure(helper, level, level.getMaxY(), Biomes.JAGGED_PEAKS);

        for (int dy = 10; dy <= 100; dy += 10) {
            int y = level.getMaxY() + dy;
            double upperPressure = EnvironmentTestHelper.getBiomePressure(helper, level, y, Biomes.JAGGED_PEAKS);
            EnvironmentTestHelper.assertPressureEquals(helper, pressure, upperPressure);
        }

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jagged_peaks_is_constant_pressure_below_min_altitude(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        double pressure = EnvironmentTestHelper.getBiomePressure(helper, level, level.getMinY(), Biomes.JAGGED_PEAKS);

        for (int dy = 10; dy <= 100; dy += 10) {
            int y = level.getMinY() - dy;
            double lowerPressure = EnvironmentTestHelper.getBiomePressure(helper, level, y, Biomes.JAGGED_PEAKS);
            EnvironmentTestHelper.assertPressureEquals(helper, pressure, lowerPressure);
        }

        helper.succeed();
    }
}