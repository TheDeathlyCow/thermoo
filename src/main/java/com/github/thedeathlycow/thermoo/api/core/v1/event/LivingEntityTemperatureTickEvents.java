package com.github.thedeathlycow.thermoo.api.core.v1.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.entity.LivingEntity;

/**
 * Events for ticking passive and active temperature changes on entities on the logical server. These events will apply
 * to spectator entities, but will not apply to dead or removed entities.
 * <p>
 * There are two categories of temperature change update ticks: passive and active. Passive changes should be used for
 * temperature changes from nearby blocks, such as heat from light sources or cooling from an air conditioner. Active
 * changes should be used for temperature changes from entity effects such as heat from being on fire, or cold from
 * being submerged in powder snow.
 * <p>
 * For environmental effects, see the {@link com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition} that handles
 * the environment datapack registries, and {@link com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents}
 * for applying temperature changes from those environmental conditions.
 * <p>
 * The events are invoked in the following order:
 * <ul><li>ALLOW_(PASSIVE|ACTIVE)_TEMPERATURE_UPDATE
 * <li>GET_(PASSIVE|ACTIVE)_TEMPERATURE_CHANGE
 * <li>ALLOW_(PASSIVE|ACTIVE)_TEMPERATURE_CHANGE</ul>
 */
public final class LivingEntityTemperatureTickEvents {
    /**
     * Checks if the passive temperature update tick for a living entity should be allowed to proceed at all. Returning
     * any non-default value will force the update to proceed right away. By default, the update will be allowed to
     * proceed.
     * <p>
     * Passive changes should be used for temperature changes from nearby blocks, such as heat from light sources or
     * cooling from an air conditioner.
     */
    public static final Event<AllowTemperatureUpdate> ALLOW_PASSIVE_TEMPERATURE_UPDATE = EventFactory.createArrayBacked(
            AllowTemperatureUpdate.class,
            listeners -> context -> {
                for (AllowTemperatureUpdate listener : listeners) {
                    TriState result = listener.allowUpdate(context);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    /**
     * Gets the passive change update that should be applied to a living entity this tick.
     * <p>
     * Passive changes should be used for temperature changes from nearby blocks, such as heat from light sources or
     * cooling from an air conditioner.
     */
    public static final Event<GetTemperatureChange> GET_PASSIVE_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            GetTemperatureChange.class,
            listeners -> context -> {
                int total = 0;
                for (GetTemperatureChange listener : listeners) {
                    total += listener.addTemperature(context);
                }
                return total;
            }
    );

    /**
     * Checks if the final passive temperature change update calculated by {@link #GET_PASSIVE_TEMPERATURE_CHANGE} should be
     * allowed to be applied to a living entity this tick. Returning any non-default value will force the update to be
     * applied right away. By default, the update will be allowed to be applied. A temperature change of 0 will not
     * invoke this event, and temperature changes of 0 will never apply.
     * <p>
     * Passive changes should be used for temperature changes from nearby blocks, such as heat from light sources or
     * cooling from an air conditioner.
     */
    public static final Event<AllowTemperatureChange> ALLOW_PASSIVE_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            AllowTemperatureChange.class,
            listeners -> (context, temperatureChange) -> {
                for (AllowTemperatureChange listener : listeners) {
                    TriState result = listener.allowChange(context, temperatureChange);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    /**
     * Checks if the active temperature update tick for a living entity should be allowed to proceed at all. Returning
     * any non-default value will force the update to proceed right away. By default, the update will be allowed to
     * proceed.
     * <p>
     * Active changes should be used for temperature changes from entity effects such as heat from being on fire, or
     * cold from being submerged in powder snow.
     */
    public static final Event<AllowTemperatureUpdate> ALLOW_ACTIVE_TEMPERATURE_UPDATE = EventFactory.createArrayBacked(
            AllowTemperatureUpdate.class,
            listeners -> context -> {
                for (AllowTemperatureUpdate listener : listeners) {
                    TriState result = listener.allowUpdate(context);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    /**
     * Gets the active change update that should be applied to a living entity this tick by summing all values supplied
     * by listeners. May be positive or negative.
     * <p>
     * Active changes should be used for temperature changes from entity effects such as heat from being on fire, or
     * cold from being submerged in powder snow.
     */
    public static final Event<GetTemperatureChange> GET_ACTIVE_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            GetTemperatureChange.class,
            listeners -> context -> {
                int total = 0;
                for (GetTemperatureChange listener : listeners) {
                    total += listener.addTemperature(context);
                }
                return total;
            }
    );

    /**
     * Checks if the final active temperature change update calculated by {@link #GET_ACTIVE_TEMPERATURE_CHANGE} should be
     * allowed to be applied to a living entity this tick. Returning any non-default value will force the update to be
     * applied right away. By default, the update will be allowed to be applied. A temperature change of 0 will not
     * invoke this event, and temperature changes of 0 will never apply.
     * <p>
     * Active changes should be used for temperature changes from entity effects such as heat from being on fire, or
     * cold from being submerged in powder snow.
     */
    public static final Event<AllowTemperatureChange> ALLOW_ACTIVE_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            AllowTemperatureChange.class,
            listeners -> (context, temperatureChange) -> {
                for (AllowTemperatureChange listener : listeners) {
                    TriState result = listener.allowChange(context, temperatureChange);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    @FunctionalInterface
    public interface AllowTemperatureUpdate {
        /**
         * Whether this listener should allow a temperature change update to begin.
         *
         * @param context Context of the living entity for the tick.
         * @return Return true or false to make the update happen right away, or default to fall back to other listeners.
         * The default behaviour will be to allow the update.
         */
        TriState allowUpdate(EnvironmentTickContext<? extends LivingEntity> context);
    }

    @FunctionalInterface
    public interface GetTemperatureChange {
        /**
         * Calculates the temperature change that this listener wants to add to a living entity this tick.
         *
         * @param context Context of the living entity for the tick.
         * @return Return the temperature point change that this listener wants to apply to the entity in the context.
         * This value is added to the values supplied by the other listeners.
         */
        int addTemperature(EnvironmentTickContext<? extends LivingEntity> context);
    }

    @FunctionalInterface
    public interface AllowTemperatureChange {
        /**
         * Whether this listener should allow a temperature change update to apply.
         *
         * @param context           Context of the living entity for the tick.
         * @param temperatureChange The actual change in temperature calculated from the {@link GetTemperatureChange} listener. This value is non-zero.
         * @return Return true or false to make the update apply right away, or default to fall back to other listeners.
         * The default behaviour will be to allow the update.
         */
        TriState allowChange(EnvironmentTickContext<? extends LivingEntity> context, int temperatureChange);
    }

    private LivingEntityTemperatureTickEvents() {

    }
}