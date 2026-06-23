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

package com.github.thedeathlycow.thermoo.gametest.tests;

import com.github.thedeathlycow.thermoo.api.entity.v1.ThermooAttributes;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;

@SuppressWarnings("unused")
public class AttributeTests {

    @GameTest
    public void villager_min_temperature_is_set_by_event(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO.above());

        helper.assertValueEqual(villager.getAttributeValue(ThermooAttributes.MIN_TEMPERATURE), 40.0, Component.literal("Min Temperature Attribute"));
        helper.assertValueEqual(villager.thermoo$getMinTemperature(), -40 * 140, Component.literal("Min Temperature Value"));
        helper.succeed();
    }

    @GameTest
    public void villager_max_temperature_is_set_by_event(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO.above());

        helper.assertValueEqual(villager.getAttributeValue(ThermooAttributes.MAX_TEMPERATURE), 40.0, Component.literal("Max Temperature Attribute"));
        helper.assertValueEqual(villager.thermoo$getMaxTemperature(), 40 * 140, Component.literal("Max Temperature Value"));
        helper.succeed();
    }
}