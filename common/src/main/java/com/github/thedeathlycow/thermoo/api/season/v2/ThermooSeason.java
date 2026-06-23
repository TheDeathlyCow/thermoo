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

package com.github.thedeathlycow.thermoo.api.season.v2;

import dev.yumi.commons.TriState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

/// Contains common functionality for all thermoo season types.
///
/// @see TemperateSeason
/// @see TropicalSeason
@ApiStatus.NonExtendable
public sealed interface ThermooSeason extends StringRepresentable permits TemperateSeason, TropicalSeason {
    /// Creates a season state that represents the start of this season.
    ThermooSeasonState<? extends ThermooSeason> createState();

    /// Convenience method for invoking [ThermooSeasonEvents#IS_COLD_ENOUGH_TO_SNOW]
    static TriState isColdEnoughToSnow(Level level, BlockPos pos) {
        Holder<Biome> biome = level.getBiomeManager().getNoiseBiomeAtPosition(pos);
        return ThermooSeasonEvents.IS_COLD_ENOUGH_TO_SNOW.invoker().isColdEnoughToSnow(level, pos, biome);
    }
}