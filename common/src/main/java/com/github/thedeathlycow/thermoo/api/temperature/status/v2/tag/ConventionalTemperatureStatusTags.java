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

package com.github.thedeathlycow.thermoo.api.temperature.status.v2.tag;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

/**
 * Conventional version of the tags defined in {@link TemperatureStatusTags} provided for those who prefer to use the
 * common 'c' namespace. Note that not all tags are aliased here, only the ones that exist exclusively for conventional
 * purposes.
 */
public final class ConventionalTemperatureStatusTags {
    /**
     * Conventional alias of {@link TemperatureStatusTags#HARMFUL}
     */
    public static final TagKey<TemperatureStatus> HARMFUL = create("harmful");

    /**
     * Conventional alias of {@link TemperatureStatusTags#BENEFICIAL}
     */
    public static final TagKey<TemperatureStatus> BENEFICIAL = create("beneficial");

    /**
     * Conventional alias of {@link TemperatureStatusTags#NEUTRAL}
     */
    public static final TagKey<TemperatureStatus> NEUTRAL = create("neutral");

    /**
     * Conventional alias of {@link TemperatureStatusTags#COLD}
     */
    public static final TagKey<TemperatureStatus> COLD = create("cold");

    /**
     * Conventional alias of {@link TemperatureStatusTags#WARM}
     */
    public static final TagKey<TemperatureStatus> WARM = create("warm");

    private static TagKey<TemperatureStatus> create(String name) {
        return TagKey.create(ThermooRegistries.TEMPERATURE_STATUS, Identifier.fromNamespaceAndPath("c", name));
    }

    private ConventionalTemperatureStatusTags() {

    }
}