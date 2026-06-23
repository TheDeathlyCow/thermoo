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

package com.github.thedeathlycow.thermoo.api.environment.v2.component;

import com.mojang.serialization.Codec;

/**
 * Stores the codec and default value for {@link EnvironmentComponentTypes#RELATIVE_HUMIDITY}
 */
public final class RelativeHumidityComponent {
    /**
     * A double codec that restricts values to a 0-1 percentage scale
     */
    public static final Codec<Double> CODEC = Codec.doubleRange(0, 1);
    /**
     * The default relative humidity, a comfortable 50%
     */
    public static final double DEFAULT = 0.5;

    private RelativeHumidityComponent() {

    }
}