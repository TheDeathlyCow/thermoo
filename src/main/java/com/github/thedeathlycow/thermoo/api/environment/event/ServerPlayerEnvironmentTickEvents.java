package com.github.thedeathlycow.thermoo.api.environment.event;

import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Events that are called for a server player each tick to update and apply passive temperature changes based on their
 * {@link com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition environment} (as defined in datapacks).
 * <p>
 * These events will apply to players in spectator mode, but will not apply to dead or removed players.
 * <p>
 * The order that the events are executed for each player each tick is as follows:
 * <ul><li>ALLOW_TEMPERATURE_UPDATE
 * <li>GET_TEMPERATURE_CHANGE
 * <li>ALLOW_TEMPERATURE_CHANGE</ul>
 */
public final class ServerPlayerEnvironmentTickEvents {
    /**
     * Checks if the temperature update should be allowed to proceed at all. Returning any non-default value will force
     * the update to proceed right away. By default, the update will be allowed to proceed.
     * <p>
     * At this stage, the context's environment components will be empty.
     */
    public static final Event<AllowTemperatureChangeUpdate> ALLOW_TEMPERATURE_UPDATE = EventFactory.createArrayBacked(
            AllowTemperatureChangeUpdate.class,
            listeners -> context -> {
                for (AllowTemperatureChangeUpdate listener : listeners) {
                    TriState result = listener.allowUpdate(context);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    /**
     * Computes the temperature point change for a temperature aware given their environment conditions. All returned
     * values will be added to together and, if allowed, will be applied in one temperature change.
     * <p>
     * From this point onwards, the context environment components will be defined, but some values may still be empty.
     */
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

    /**
     * Checks if the final temperature change update calculated by {@link #GET_TEMPERATURE_CHANGE} should be allowed to
     * be applied to the player. Returning any non-default value will force the update to be applied right away. By
     * default, the update will be allowed to be applied. A temperature change of 0 will not invoke this event, and
     * temperature changes of 0 will never apply.
     */
    public static final Event<AllowTemperatureChangeApply> ALLOW_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            AllowTemperatureChangeApply.class,
            listeners -> (context, temperatureChange) -> {
                for (AllowTemperatureChangeApply listener : listeners) {
                    TriState result = listener.allowTemperatureChange(context, temperatureChange);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    @FunctionalInterface
    public interface AllowTemperatureChangeUpdate {
        TriState allowUpdate(EnvironmentTickContext<ServerPlayerEntity> context);
    }

    @FunctionalInterface
    public interface GetTemperatureChange {
        int addPointChange(EnvironmentTickContext<ServerPlayerEntity> context);
    }

    @FunctionalInterface
    public interface AllowTemperatureChangeApply {
        TriState allowTemperatureChange(EnvironmentTickContext<ServerPlayerEntity> context, int temperatureChange);
    }

    private ServerPlayerEnvironmentTickEvents() {

    }
}