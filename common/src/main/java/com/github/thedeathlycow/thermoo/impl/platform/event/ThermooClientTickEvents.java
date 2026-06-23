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