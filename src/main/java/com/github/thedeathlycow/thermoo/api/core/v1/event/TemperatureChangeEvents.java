package com.github.thedeathlycow.thermoo.api.core.v1.event;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.entity.LivingEntity;

public class TemperatureChangeEvents {
    /**
     * Checks if the passive temperature update tick for a living entity should be allowed to proceed at all. Returning
     * any non-default value will force the update to proceed right away. By default, the update will be allowed to
     * proceed.
     * <p>
     * Passive changes should be used for temperature changes from nearby blocks, such as heat from light sources or
     * cooling from an air conditioner.
     */
    public static final Event<AllowChange> ALLOW_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            AllowChange.class,
            listeners -> (target, change, context) -> {
                for (AllowChange listener : listeners) {
                    TriState result = listener.allowChange(target, change, context);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
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
    public static final Event<AfterChange> AFTER_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(
            AfterChange.class,
            listeners -> (target, oldTemperature, newTemperature, context) -> {
                for (AfterChange listener : listeners) {
                    listener.afterChange(target, oldTemperature, newTemperature, context);
                }
            }
    );


    @FunctionalInterface
    public interface AllowChange {
        /**
         * Whether this listener should allow a temperature point change update to apply.
         *
         * @param context Context of the living entity for the tick.
         * @param change  The attempted change in temperature points. This value is non-zero.
         * @return Return true or false to make the update apply right away, or default to fall back to other listeners.
         * The default behaviour will be to allow the update.
         */
        TriState allowChange(LivingEntity target, int change, TemperatureChange context);
    }

    @FunctionalInterface
    public interface AfterChange {
        void afterChange(LivingEntity target, int oldTemperature, int newTemperature, TemperatureChange context);
    }
}