package com.github.thedeathlycow.thermoo.api.environment.v2.event;

import com.github.thedeathlycow.thermoo.api.core.v2.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.commons.event.Event;
import dev.yumi.commons.TriState;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * Events that are called for a server player each tick to update and apply passive temperature changes based on their
 * {@link EnvironmentDefinition environment} (as defined in datapacks).
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
    public static final Event<Identifier, AllowTemperatureChangeUpdate> ALLOW_TEMPERATURE_UPDATE = Thermoo.EVENT_MANAGER.create(
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
    public static final Event<Identifier, GetTemperatureChange> GET_TEMPERATURE_CHANGE = Thermoo.EVENT_MANAGER.create(
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
    public static final Event<Identifier, AllowTemperatureChangeApply> ALLOW_TEMPERATURE_CHANGE = Thermoo.EVENT_MANAGER.create(
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
        TriState allowUpdate(EnvironmentTickContext<ServerPlayer> context);
    }

    @FunctionalInterface
    public interface GetTemperatureChange {
        int addPointChange(EnvironmentTickContext<ServerPlayer> context);
    }

    @FunctionalInterface
    public interface AllowTemperatureChangeApply {
        TriState allowTemperatureChange(EnvironmentTickContext<ServerPlayer> context, int temperatureChange);
    }

    private ServerPlayerEnvironmentTickEvents() {

    }
}