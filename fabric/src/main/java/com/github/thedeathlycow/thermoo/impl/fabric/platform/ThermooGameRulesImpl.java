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

package com.github.thedeathlycow.thermoo.impl.fabric.platform;

import com.github.thedeathlycow.thermoo.impl.platform.ThermooGameRules;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;

public class ThermooGameRulesImpl implements ThermooGameRules {
    @Override
    public GameRule<Boolean> forBoolean(Identifier id, boolean defaultValue) {
        return GameRuleBuilder.forBoolean(defaultValue).buildAndRegister(id);
    }

    @Override
    public <E extends Enum<E>> GameRule<E> forEnum(Identifier id, E defaultValue, Codec<E> codec) {
        return GameRuleBuilder.forEnum(defaultValue).codec(codec).buildAndRegister(id);
    }
}