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

package com.github.thedeathlycow.thermoo.impl.client;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugEnvironmentComponents;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugEnvironments;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugSeasons;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugSelfStatuses;
import com.github.thedeathlycow.thermoo.mixin.client.DebugScreenEntriesAccessor;

public final class ThermooClientRegisters {
    public static void registerDebugEntries() {
        DebugScreenEntriesAccessor.thermoo_invokeRegister(Thermoo.id("environment_components"), new DebugEnvironmentComponents());
        DebugScreenEntriesAccessor.thermoo_invokeRegister(Thermoo.id("environments"), new DebugEnvironments());
        DebugScreenEntriesAccessor.thermoo_invokeRegister(Thermoo.id("seasons"), new DebugSeasons());
        DebugScreenEntriesAccessor.thermoo_invokeRegister(Thermoo.id("self_status"), new DebugSelfStatuses());
    }

    private ThermooClientRegisters() {

    }
}