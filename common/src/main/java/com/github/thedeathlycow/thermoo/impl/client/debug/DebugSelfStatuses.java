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

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugSelfStatuses implements ThermooDebugScreenEntry {
    public static final Identifier GROUP = Thermoo.id("entity");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Minecraft minecraft = Minecraft.getInstance();
        @Nullable Entity cameraEntity = minecraft.getCameraEntity();

        if (cameraEntity instanceof LivingEntity livingEntity && level instanceof ServerLevel serverLevel) {
            displayer.addToGroup(
                    GROUP,
                    "Temperature: %d (%.2f%%)".formatted(
                            livingEntity.thermoo$getTemperature(),
                            livingEntity.thermoo$getTemperatureScale() * 100
                    )
            );

            displayer.addToGroup(
                    GROUP,
                    "Soaking: %d (%.2f%%)".formatted(
                            livingEntity.thermoo$getWetTicks(),
                            livingEntity.thermoo$getSoakedScale() * 100
                    )
            );
        }
    }
}