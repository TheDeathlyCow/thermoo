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

package com.github.thedeathlycow.thermoo.api.item.v2;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Tag keys for Thermoo's provided {@linkplain Item item tags}.
 */
public final class ThermooItemTags {
    /**
     * Conventional tag for consumables that are warming (has no effects with Thermoo alone)
     */
    public static final TagKey<Item> CONSUMABLE_WARMING = create("consumable/warming");

    /**
     * Conventional tag for consumables that are cooling (has no effects with Thermoo alone)
     */
    public static final TagKey<Item> CONSUMABLE_COOLING = create("consumable/cooling");

    private static TagKey<Item> create(String path) {
        return TagKey.create(Registries.ITEM, Thermoo.id(path));
    }

    private ThermooItemTags() {

    }
}
