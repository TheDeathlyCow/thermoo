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
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;

/// A platform independent abstraction for a Server Lifecycle Events, based on the equivalent class provided by Fabric
///
/// This is not a stable API!
public final class ThermooServerLifecycleEvents {
    public static final Event<Identifier, ServerStarted> SERVER_STARTED = Thermoo.IMPL_EVENT_MANAGER.create(
            ServerStarted.class,
            listeners -> server -> {
                for (ServerStarted listener : listeners) {
                    listener.onServerStarted(server);
                }
            });

    public static final Event<Identifier, ServerStopped> SERVER_STOPPED = Thermoo.IMPL_EVENT_MANAGER.create(
            ServerStopped.class,
            listeners -> server -> {
                for (ServerStopped listener : listeners) {
                    listener.onServerStopped(server);
                }
            });

    @FunctionalInterface
    public interface ServerStarted {
        void onServerStarted(MinecraftServer server);
    }

    @FunctionalInterface
    public interface ServerStopped {
        void onServerStopped(MinecraftServer server);
    }
}