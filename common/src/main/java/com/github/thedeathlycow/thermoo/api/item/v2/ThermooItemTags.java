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
