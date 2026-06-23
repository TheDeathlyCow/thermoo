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

package com.github.thedeathlycow.thermoo.impl.fabric;

import com.github.thedeathlycow.thermoo.impl.ThermooClient;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentClientModInitializer;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Arrays;
import java.util.List;

public class ThermooFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        ThermooClient.onInitializeClient(mod);
        initializeDependentEntryPoints();
    }

    private static void initializeDependentEntryPoints() {
        List<DependentClientModInitializer> initializers = FabricLoader.getInstance().getEntrypoints(
                DependentClientModInitializer.ID,
                DependentClientModInitializer.class
        );

        for (DependentClientModInitializer initializer : initializers) {
            boolean initialize = Arrays.stream(initializer.getRequiredModIds()).allMatch(
                    id -> FabricLoader.getInstance().isModLoaded(id)
            );

            if (initialize) {
                initializer.onInitializeClient();
            }
        }
    }
}