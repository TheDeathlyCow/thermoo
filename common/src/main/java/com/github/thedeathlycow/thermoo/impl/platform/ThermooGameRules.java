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

package com.github.thedeathlycow.thermoo.impl.platform;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;

/// Platform independent API for creating unit tests. Based on the Fabric API GameRuleBuilder. Mostly meant for testing.
///
/// This is not a stable API!
public interface ThermooGameRules {
    GameRule<Boolean> forBoolean(Identifier id, boolean defaultValue);

    <E extends Enum<E>> GameRule<E> forEnum(Identifier id, E defaultValue, Codec<E> codec);
}
