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

package com.github.thedeathlycow.thermoo.api.command.v1;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureUnit;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.util.StringRepresentable;

/**
 * An argument type to specify {@link TemperatureUnit}s.
 */
public final class TemperatureUnitArgument extends StringRepresentableArgument<TemperatureUnit> {
    public static final Codec<TemperatureUnit> CODEC = StringRepresentable.fromEnum(TemperatureUnit::values);

    private TemperatureUnitArgument() {
        super(CODEC, TemperatureUnit::values);
    }

    public static TemperatureUnitArgument temperatureUnit() {
        return new TemperatureUnitArgument();
    }

    public static TemperatureUnit getTemperatureUnit(CommandContext<CommandSourceStack> context, String id) {
        return context.getArgument(id, TemperatureUnit.class);
    }
}
