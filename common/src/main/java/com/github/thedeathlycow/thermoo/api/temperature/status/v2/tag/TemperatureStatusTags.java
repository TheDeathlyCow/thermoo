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
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.tags.TagKey;

/**
 * Builtin tag keys for {@linkplain TemperatureStatus temperature statuses}.
 */
public final class TemperatureStatusTags {
    /**
     * Specifies the order in which temperature statuses are applied to an entity.
     * <p>
     * Statuses included in this tag are processed first, following the order defined in the tag. Any statuses not
     * present in this tag are processed afterwards in an undefined order.
     */
    public static final TagKey<TemperatureStatus> APPLICATION_ORDER = create("application_order");

    /**
     * Statuses that are harmful to their targets.
     */
    public static final TagKey<TemperatureStatus> HARMFUL = create("harmful");

    /**
     * Statuses that are beneficial to their targets.
     */
    public static final TagKey<TemperatureStatus> BENEFICIAL = create("beneficial");

    /**
     * Statuses that are neither harmful nor beneficial.
     */
    public static final TagKey<TemperatureStatus> NEUTRAL = create("neutral");

    /**
     * Statuses that are applied as a result of being {@link TemperatureAware#thermoo$isCold() cold}.
     */
    public static final TagKey<TemperatureStatus> COLD = create("cold");

    /**
     * Statuses that are applied as a result of being {@link TemperatureAware#thermoo$isWarm() warm}.
     */
    public static final TagKey<TemperatureStatus> WARM = create("warm");

    private static TagKey<TemperatureStatus> create(String name) {
        return TagKey.create(ThermooRegistries.TEMPERATURE_STATUS, Thermoo.id(name));
    }

    private TemperatureStatusTags() {

    }
}