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

package com.github.thedeathlycow.thermoo.gametest.tests.temperature.status;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusManager;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;

import java.util.List;

@SuppressWarnings("unused")
public class TemperatureStatusManagerTest {
    @GameTest
    public void applicationOrderSetsLookupOrder(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        HolderLookup<TemperatureStatus> lookup = level.holderLookup(ThermooRegistries.TEMPERATURE_STATUS);

        List<Identifier> statuses = TemperatureStatusManager.lookup(EntityType.PLAYER.builtInRegistryHolder(), lookup)
                .stream()
                .map(ref -> ref.key().identifier())
                .toList();

        List<Identifier> expected = List.of(
                        lookup.getOrThrow(key("ordered/first")),
                        lookup.getOrThrow(key("ordered/second")),
                        lookup.getOrThrow(key("ordered/third")),
                        lookup.getOrThrow(key("ordered/fourth")),
                        lookup.getOrThrow(key("attribute_test")),
                        lookup.getOrThrow(key("function_test")),
                        lookup.getOrThrow(key("damage_test")),
                        lookup.getOrThrow(key("mod_exists_test")),
                        lookup.getOrThrow(key("mob_effect_test")),
                        lookup.getOrThrow(key("disabled_by_default")),
                        lookup.getOrThrow(key("scaling_attribute_test")) // tests that items not in tag are last
                ).stream()
                .map(ref -> ref.key().identifier())
                .toList();

        helper.assertValueEqual(statuses, expected, "Temperature Status Order");

        helper.succeed();
    }

    @GameTest
    public void modDoesNotExistTestIsNotLoaded(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        HolderLookup<TemperatureStatus> lookup = level.holderLookup(ThermooRegistries.TEMPERATURE_STATUS);

        helper.assertTrue(lookup.get(key("mod_does_not_exist_test")).isEmpty(), "Effect did not respect load conditions");

        helper.succeed();
    }

    @GameTest
    public void modExistsTestIsLoaded(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        HolderLookup<TemperatureStatus> lookup = level.holderLookup(ThermooRegistries.TEMPERATURE_STATUS);

        helper.assertTrue(lookup.get(key("mod_exists_test")).isPresent(), "Effect did not respect load conditions");

        helper.succeed();
    }

    private ResourceKey<TemperatureStatus> key(String name) {
        return ResourceKey.create(ThermooRegistries.TEMPERATURE_STATUS, ThermooTestMod.id(name));
    }
}