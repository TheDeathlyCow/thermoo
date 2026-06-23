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

package com.github.thedeathlycow.thermoo.api.entity.v1;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

/**
 * Tag keys for Thermoo's provided {@linkplain EntityType entity type tags}.
 */
public final class ThermooEntityTypeTags {
    /**
     * Entity types that benefit from being cold
     */
    public static final TagKey<EntityType<?>> BENEFITS_FROM_COLD_ENTITY_TYPE = create("benefits_from_cold");

    /**
     * Entity types that benefit from being warm
     */
    public static final TagKey<EntityType<?>> BENEFITS_FROM_HEAT_ENTITY_TYPE = create("benefits_from_heat");

    /**
     * Entity types that are cold immune
     */
    public static final TagKey<EntityType<?>> COLD_IMMUNE_ENTITY_TYPE = create("cold_immune");

    /**
     * Entity types that are heat immune
     */
    public static final TagKey<EntityType<?>> HEAT_IMMUNE_ENTITY_TYPE = create("heat_immune");

    private static TagKey<EntityType<?>> create(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Thermoo.id(path));
    }

    private ThermooEntityTypeTags() {

    }
}