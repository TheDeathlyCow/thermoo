package com.github.thedeathlycow.thermoo.impl.fabric.platform.event;

import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooServerLifecycleEvents;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class ThermooServerLifecycleEventsImpl implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ThermooServerLifecycleEvents.SERVER_STARTED.invoker().onServerStarted(server);
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            ThermooServerLifecycleEvents.SERVER_STOPPED.invoker().onServerStopped(server);
        });
    }
}