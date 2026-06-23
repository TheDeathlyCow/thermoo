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

package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.core.v2.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.environment.v2.event.ServerPlayerEnvironmentTickEvents;
import dev.yumi.commons.TriState;
import net.minecraft.server.level.ServerPlayer;

public final class ServerPlayerTickUtil {
    public static void invokePlayerTemperatureEvents(EnvironmentTickContext<ServerPlayer> context) {
        if (ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_UPDATE.invoker().allowUpdate(context) == TriState.FALSE) {
            return;
        }

        int temperatureChange = ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.invoker().addPointChange(context);

        if (temperatureChange != 0 && invokeAllowChange(context, temperatureChange)) {
            context.affected().thermoo$addTemperature(temperatureChange, context.level().thermoo$temperatureSources().environment());
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