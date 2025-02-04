package com.github.thedeathlycow.thermoo.api.environment.event;

import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.network.ServerPlayerEntity;

public final class ServerPlayerEnvironmentTickEvents {
    public static final Event<GetTemperatureChange> GET_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            GetTemperatureChange.class,
            listeners -> context -> {
                int value = 0;
                for (GetTemperatureChange listener : listeners) {
                    value += listener.addPointChange(context);
                }
                return value;
            }
    );

    public static final Event<AllowTemperatureChange> ALLOW_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            AllowTemperatureChange.class,
            listeners -> (context, temperatureChange) -> {
                TriState oldEventResult = PlayerEnvironmentEvents.CAN_APPLY_PASSIVE_TEMPERATURE_CHANGE.invoker().canApplyChange(
                        temperatureChange,
                        context.affected()
                );
                if (oldEventResult != TriState.DEFAULT) {
                    return oldEventResult;
                }

                for (AllowTemperatureChange listener : listeners) {
                    TriState result = listener.allowTemperatureChange(context, temperatureChange);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    @FunctionalInterface
    public interface GetTemperatureChange {
        int addPointChange(EnvironmentTickContext<ServerPlayerEntity> context);
    }

    @FunctionalInterface
    public interface AllowTemperatureChange {
        TriState allowTemperatureChange(EnvironmentTickContext<ServerPlayerEntity> context, int temperatureChange);
    }

    private ServerPlayerEnvironmentTickEvents() {

    }
}