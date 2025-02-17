package com.github.thedeathlycow.thermoo.api.temperature.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;

/**
 * Events for ticking soaking changes on entities on the logical server. These events will apply to spectator entities,
 * but will not apply to dead or removed entities.
 * <p>
 * These events are invoked in the following order:
 * <ul><li>ALLOW_SOAKING_UPDATE</li>
 * <li>ADD_SOAKING_CHANGE</li>
 * <li>ALLOW_SOAKING_CHANGE</li></ul>
 */
public final class LivingEntitySoakingTickEvents {
    /**
     * Checks if the soaking change update tick for a living entity should be allowed to proceed. Returning any non-default
     * value will force the update to proceed right away. By default, the update will be allowed to proceed.
     */
    public static final Event<AllowSoakingUpdate> ALLOW_SOAKING_UPDATE = EventFactory.createArrayBacked(
            AllowSoakingUpdate.class,
            listeners -> context -> {
                for (AllowSoakingUpdate listener : listeners) {
                    TriState result = listener.allowUpdate(context);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    /**
     * Gets the soaking change increase that should be applied to a living entity this update by summing all values
     * supplied by listeners.
     * <p>
     * If the listeners return a sum total of 0, then this event will return -1 (to dry out entities).
     */
    public static final Event<GetSoakingChange> ADD_SOAKING_CHANGE = EventFactory.createArrayBacked(
            GetSoakingChange.class,
            listeners -> context -> {
                int total = 0;
                for (GetSoakingChange listener : listeners) {
                    int change = listener.addChange(context);
                    total += change;
                }
                return total == 0 ? -1 : total;
            }
    );

    /**
     * Checks if the final soaking change update calculated by {@link #ADD_SOAKING_CHANGE}
     * should be allowed to be applied to a living entity this tick. Returning any non-default value will force the
     * update to be applied right away. By default, the update will be allowed to be applied.
     */
    public static final Event<AllowSoakingChange> ALLOW_SOAKING_CHANGE = EventFactory.createArrayBacked(
            AllowSoakingChange.class,
            listeners -> (context, soakingChange) -> {
                for (AllowSoakingChange listener : listeners) {
                    TriState result = listener.allowChange(context, soakingChange);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    @FunctionalInterface
    public interface AllowSoakingUpdate {
        /**
         * Whether this listener should allow a soaking change update to begin.
         *
         * @param context Context of the living entity for the tick.
         * @return Return true or false to make the update happen right away, or default to fall back to other listeners.
         * The default behaviour will be to allow the update.
         */
        TriState allowUpdate(TickContext<LivingEntity> context);
    }

    @FunctionalInterface
    public interface GetSoakingChange {
        /**
         * Calculates the soaking tick change that this listener wants to add to a living entity this tick.
         *
         * @param context Context of the living entity for the tick.
         * @return Return the soaking tick change that this listener wants to apply to the entity in
         * the context. This value is added to the values supplied by the other listeners.
         */
        int addChange(TickContext<LivingEntity> context);
    }

    @FunctionalInterface
    public interface AllowSoakingChange {
        /**
         * Whether this listener should allow a soaking change update to apply.
         *
         * @param context       Context of the living entity for the tick.
         * @param soakingChange The actual change in soaking ticks calculated from the {@link GetSoakingChange} events. This value is non-zero.
         * @return Return true or false to make the update apply right away, or default to fall back to other listeners.
         * The default behaviour will be to allow the update.
         */
        TriState allowChange(TickContext<LivingEntity> context, int soakingChange);
    }

    private LivingEntitySoakingTickEvents() {

    }
}