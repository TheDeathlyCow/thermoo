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

package com.github.thedeathlycow.thermoo.impl.fabric.platform.event;

import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooCommandRegistrationCallback;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ThermooCommandRegistrationCallbackImpl implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {
            ThermooCommandRegistrationCallback.EVENT.invoker().register(dispatcher, buildContext, selection);
        });
    }
}