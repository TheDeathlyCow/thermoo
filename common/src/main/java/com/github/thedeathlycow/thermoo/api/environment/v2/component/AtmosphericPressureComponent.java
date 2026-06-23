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

package com.github.thedeathlycow.thermoo.api.environment.v2.component;

import com.mojang.serialization.Codec;

/**
 * Stores the codec and default value for {@link EnvironmentComponentTypes#ATMOSPHERIC_PRESSURE}
 */
public final class AtmosphericPressureComponent {
    /**
     * A double codec that requires positive values.
     */
    public static final Codec<Double> CODEC = Codec.doubleRange(0.0, Double.MAX_VALUE);
    
    /**
     * The default atmospheric pressure, 1013.25mbar or exactly 1 standard atmosphere.
     */
    public static final double DEFAULT = 1_013.25;

    private AtmosphericPressureComponent() {

    }
}