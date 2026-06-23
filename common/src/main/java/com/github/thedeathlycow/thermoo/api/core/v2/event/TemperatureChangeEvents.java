/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.api.core.v2.event;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.commons.event.Event;
import dev.yumi.commons.TriState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

/**
 * Events that allow for finer grained control over all temperature changes.
 */
public final class TemperatureChangeEvents {
    /**
     * Checks if a given temperature change should be allowed to proceed. Hooked in after basic checks like
     * {@link TemperatureAware#thermoo$canFreeze()} and {@link TemperatureAware#thermoo$canOverheat()}.
     */
    public static final Event<Identifier, AllowChange> ALLOW_TEMPERATURE_CHANGE = Thermoo.EVENT_MANAGER.create(
            AllowChange.class,
            listeners -> (target, change, reducedChange, context) -> {
                for (AllowChange listener : listeners) {
                    TriState result = listener.allowChange(target, change, reducedChange, context);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    /**
     * Invoked after a temperature change is applied.
     */
    public static final Event<Identifier, AfterChange> AFTER_TEMPERATURE_CHANGE = Thermoo.EVENT_MANAGER.create(
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
         * @param target        The target of the temperature change.
         * @param change        The attempted change in temperature points prior to reduction. This value is non-zero.
         * @param reducedChange The actual change in temperature points after reduction. This value is non-zero.
         * @param context       Additional context of the temperature change.
         * @return Return true or false to make the update apply right away, or default to fall back to other listeners.
         * The default behavior will be to allow the update.
         */
        TriState allowChange(LivingEntity target, int change, int reducedChange, TemperatureChange context);
    }

    @FunctionalInterface
    public interface AfterChange {
        /**
         * Invoked after the change is applied.
         *
         * @param target         The target of the temperature change.
         * @param oldTemperature The target's temperature prior to the change.
         * @param newTemperature The target's temperature after the change.
         * @param context        Additional context of the temperature change.
         */
        void afterChange(LivingEntity target, int oldTemperature, int newTemperature, TemperatureChange context);
    }

    private TemperatureChangeEvents() {

    }
}