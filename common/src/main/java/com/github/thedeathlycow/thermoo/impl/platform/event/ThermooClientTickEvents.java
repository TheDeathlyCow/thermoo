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

package com.github.thedeathlycow.thermoo.impl.platform.event;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.commons.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public final class ThermooClientTickEvents {
    public static final Event<Identifier, EndTick> END_CLIENT_TICK = Thermoo.IMPL_EVENT_MANAGER.create(
            EndTick.class,
            listeners -> client -> {
                for (EndTick listener : listeners) {
                    listener.onEndTick(client);
                }
            }
    );

    @FunctionalInterface
    public interface EndTick {
        void onEndTick(Minecraft client);
    }

    private ThermooClientTickEvents() {

    }
}