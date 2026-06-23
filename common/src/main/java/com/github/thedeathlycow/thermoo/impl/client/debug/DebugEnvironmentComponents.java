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

package com.github.thedeathlycow.thermoo.impl.client.debug;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooBuiltInRegistries;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentLookup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class DebugEnvironmentComponents implements ThermooDebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Minecraft minecraft = Minecraft.getInstance();
        @Nullable Entity cameraEntity = minecraft.getCameraEntity();

        if (cameraEntity != null) {
            DataComponentMap components = EnvironmentLookup.getInstance()
                    .findEnvironmentComponents(level, cameraEntity.blockPosition());

            List<String> displayed = components.stream()
                    .map(component -> {
                        String name = Util.getRegisteredName(ThermooBuiltInRegistries.ENVIRONMENT_COMPONENT_TYPE, component.type());
                        return "%s: %s".formatted(name, component.value().toString());
                    }).toList();

            displayer.addToGroup(DebugEnvironments.GROUP, "Environment components:");
            displayer.addToGroup(DebugEnvironments.GROUP, displayed);
        }
    }
}