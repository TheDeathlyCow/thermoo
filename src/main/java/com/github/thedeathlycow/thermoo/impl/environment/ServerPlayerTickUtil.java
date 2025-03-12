package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.network.ServerPlayerEntity;

public final class ServerPlayerTickUtil {
    public static void invokePlayerTemperatureEvents(EnvironmentTickContext<ServerPlayerEntity> context) {
        if (ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_UPDATE.invoker().allowUpdate(context) == TriState.FALSE) {
            return;
        }

        int temperatureChange = ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.invoker().addPointChange(context);

        if (temperatureChange != 0 && invokeAllowChange(context, temperatureChange)) {
            context.affected().thermoo$addTemperature(temperatureChange, EnvironmentHeatingMode.INSTANCE);
        }
    }

    private static boolean invokeAllowChange(EnvironmentTickContext<ServerPlayerEntity> context, int temperatureChange) {
        TriState result = ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_CHANGE.invoker()
                .allowTemperatureChange(context, temperatureChange);
        return result != TriState.FALSE;
    }

    private ServerPlayerTickUtil() {
    }
}