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

package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.commons.TriState;
import dev.yumi.commons.event.Event;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

/// Events related to [temperature statuses][TemperatureStatus].
public final class TemperatureStatusEvents {
    /// Allows listeners to override whether an enabled temperature status is applied this tick.
    ///
    /// - Return [TriState#FALSE] to suppress the status this tick.
    /// - Return [TriState#TRUE] to explicitly allow it.
    /// - Return [TriState#DEFAULT] to abstain; the status will be allowed unless another listener suppresses it.
    ///
    /// If any listener returns a non-[DEFAULT][TriState#DEFAULT] result, subsequent listeners are not invoked.
    ///
    /// This event only fires for statuses that are already [enabled][TemperatureStatusLookup#isEnabled]. It cannot be
    /// used to forcibly apply a disabled status. Use [TemperatureStatusLookup#setEnabled] for persistent control.
    public static final Event<Identifier, AllowTemperatureStatus> ALLOW_TEMPERATURE_STATUS = Thermoo.EVENT_MANAGER.create(
            AllowTemperatureStatus.class,
            listeners -> (entity, statusReference) -> {
                for (AllowTemperatureStatus listener : listeners) {
                    TriState result = listener.allow(entity, statusReference);

                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }

                return TriState.DEFAULT;
            }
    );

    @FunctionalInterface
    public interface AllowTemperatureStatus {
        TriState allow(LivingEntity entity, Holder.Reference<TemperatureStatus> statusReference);
    }

    private TemperatureStatusEvents() {

    }
}