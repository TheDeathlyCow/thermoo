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