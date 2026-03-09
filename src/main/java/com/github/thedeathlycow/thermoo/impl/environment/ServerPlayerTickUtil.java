package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.core.v1.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.level.ServerPlayer;

public final class ServerPlayerTickUtil {
    public static void invokePlayerTemperatureEvents(EnvironmentTickContext<ServerPlayer> context) {
        if (ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_UPDATE.invoker().allowUpdate(context) == TriState.FALSE) {
            return;
        }

        int temperatureChange = ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.invoker().addPointChange(context);

        if (temperatureChange != 0 && invokeAllowChange(context, temperatureChange)) {
            context.affected().thermoo$addTemperature(temperatureChange, context.temperatureSources().environment());
        }
    }

    private static boolean invokeAllowChange(EnvironmentTickContext<ServerPlayer> context, int temperatureChange) {
        TriState result = ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_CHANGE.invoker()
                .allowTemperatureChange(context, temperatureChange);
        return result != TriState.FALSE;
    }

    private ServerPlayerTickUtil() {
    }
}