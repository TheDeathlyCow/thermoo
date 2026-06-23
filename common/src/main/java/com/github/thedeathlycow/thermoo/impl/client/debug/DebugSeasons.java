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

import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeasonState;
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class DebugSeasons implements ThermooDebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Minecraft minecraft = Minecraft.getInstance();
        @Nullable Entity cameraEntity = minecraft.getCameraEntity();

        if (level != null && cameraEntity != null) {
            Optional<ThermooSeasonState<TemperateSeason>> temperateSeason = TemperateSeason.getCurrentState(level, cameraEntity.blockPosition());
            Optional<ThermooSeasonState<TropicalSeason>> tropicalSeason = TropicalSeason.getCurrentState(level, cameraEntity.blockPosition());

            this.addLine(displayer, "Temperate Season", temperateSeason);
            this.addLine(displayer, "Tropical Season", tropicalSeason);
        }
    }

    private <S extends ThermooSeason> void addLine(DebugScreenDisplayer displayer, String name, Optional<ThermooSeasonState<S>> result) {
        if (result.isPresent()) {
            ThermooSeasonState<?> state = result.get();
            displayer.addToGroup(DebugEnvironments.GROUP, "%s: %s (%s%%)".formatted(name, state.season().getSerializedName(), state.progress() * 100));
        } else {
            displayer.addToGroup(DebugEnvironments.GROUP, "%s: [None]".formatted(name));
        }
    }
}