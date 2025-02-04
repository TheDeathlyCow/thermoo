package com.github.thedeathlycow.thermoo.api.environment.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public final class EnvironmentTickEvents {
    public static final Event<TickPlayerEnvironmentTemperature> PLAYER_ENVIRONMENT_TEMPERATURE = EventFactory.createArrayBacked(
            TickPlayerEnvironmentTemperature.class,
            listeners -> context -> {
                int value = 0;
                for (TickPlayerEnvironmentTemperature listener : listeners) {
                    value += listener.addPointChange(context);
                }
                return value;
            }
    );

    @FunctionalInterface
    public interface TickPlayerEnvironmentTemperature {
        int addPointChange(EnvironmentTickContext<ServerPlayerEntity> context);
    }

    private EnvironmentTickEvents() {

    }
}