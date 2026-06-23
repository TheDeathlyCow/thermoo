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

package com.github.thedeathlycow.thermoo.impl.platform.event;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.brigadier.CommandDispatcher;
import dev.yumi.commons.event.Event;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.Identifier;

/// A platform independent abstraction for a Command Registration API, based on the equivalent class provided by Fabric
///
/// This is not a stable API!
public interface ThermooCommandRegistrationCallback {
    Event<Identifier, ThermooCommandRegistrationCallback> EVENT = Thermoo.IMPL_EVENT_MANAGER.create(
            ThermooCommandRegistrationCallback.class,
            listeners -> (dispatcher, buildContext, selection) -> {
                for (ThermooCommandRegistrationCallback listener : listeners) {
                    listener.register(dispatcher, buildContext, selection);
                }
            });

    void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection);
}