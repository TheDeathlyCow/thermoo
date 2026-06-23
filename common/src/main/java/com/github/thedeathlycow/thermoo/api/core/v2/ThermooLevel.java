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

package com.github.thedeathlycow.thermoo.api.core.v2;

import com.github.thedeathlycow.thermoo.api.core.v2.source.BuiltinTemperatureSources;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;

/**
 * Thermoo extensions of {@link net.minecraft.world.level.Level}.
 * <p>
 * Note: This interface is automatically implemented on all levels via Mixin and interface injection.
 */
@ApiStatus.NonExtendable
public interface ThermooLevel {
    /**
     * A manager of shared instances of {@link TemperatureChange}. This is analogous to {@link Level#damageSources()}
     */
    default BuiltinTemperatureSources thermoo$temperatureSources() {
        throw new NotImplementedException();
    }
}