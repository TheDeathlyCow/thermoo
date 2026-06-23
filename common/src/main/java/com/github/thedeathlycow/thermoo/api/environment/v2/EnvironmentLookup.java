/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
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

package com.github.thedeathlycow.thermoo.api.environment.v2;

import com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

/**
 * This interface provides facilities for looking up environment values from the {@link EnvironmentDefinition}s.
 * <p>
 * Should only be extended by Thermoo.
 */
@ApiStatus.NonExtendable
public interface EnvironmentLookup {
    /**
     * Gets the singleton instance of this interface
     */
    static EnvironmentLookup getInstance() {
        return EnvironmentLookupImpl.INSTANCE;
    }

    /**
     * Looks up the current environment parameters for a world position
     *
     * @param world The world/level to lookup
     * @param pos   The position to lookup at
     * @return Returns an environment component map whose keys are defined by {@link EnvironmentComponentTypes}
     */
    DataComponentMap findEnvironmentComponents(Level world, BlockPos pos);
}