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

import com.github.thedeathlycow.thermoo.api.environment.v2.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class TropicalJungleHumidityTests {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jungle_fallback_humidity_is_normal_fallback(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(helper, RelativeHumidityComponent.DEFAULT, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.WET)
    public void jungle_wet_humidity_is_100pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.WET);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(helper, 1.0, humidity);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.DRY)
    public void jungle_dry_humidity_is_25pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, TropicalSeason.DRY);

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.JUNGLE);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.25, humidity);

        helper.succeed();
    }
}