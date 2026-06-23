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

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;

@SuppressWarnings("unused")
public class EnvironmentPriorityTests {
    @GameTest
    public void environments_loaded_in_priority_order(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Registry<EnvironmentDefinition> registry = level.registryAccess().lookupOrThrow(ThermooRegistries.ENVIRONMENT);
        Holder<Biome> netherWastes = level.registryAccess()
                .lookupOrThrow(Registries.BIOME)
                .getOrThrow(Biomes.NETHER_WASTES);

        List<Identifier> loadedEnvironments = EnvironmentLookupImpl.getAllMatchingEnvironments(netherWastes, registry)
                .map(registry::getKey)
                .toList();

        List<Identifier> expectedEnvironments = List.of(
                ThermooTestMod.id("priority/high_priority"),
                ThermooTestMod.id("priority/default_priority"),
                ThermooTestMod.id("priority/low_priority")
        );

        helper.assertValueEqual(loadedEnvironments, expectedEnvironments, Component.literal("Nether Wastes Environment Providers"));
        helper.succeed();
    }
}