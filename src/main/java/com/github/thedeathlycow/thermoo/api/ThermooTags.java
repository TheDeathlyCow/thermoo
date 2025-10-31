package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * All tags used by Thermoo. Thermoo by default leaves these tags as empty - even for vanilla entries
 */
public final class ThermooTags {

    /**
     * Entity types that benefit from being cold
     */
    public static final TagKey<EntityType<?>> BENEFITS_FROM_COLD_ENTITY_TYPE = createEntityTypeTag("benefits_from_cold");

    /**
     * Entity types that benefit from being warm
     */
    public static final TagKey<EntityType<?>> BENEFITS_FROM_HEAT_ENTITY_TYPE = createEntityTypeTag("benefits_from_heat");

    /**
     * Entity types that are cold immune
     */
    public static final TagKey<EntityType<?>> COLD_IMMUNE_ENTITY_TYPE = createEntityTypeTag("cold_immune");

    /**
     * Entity types that are heat immune
     */
    public static final TagKey<EntityType<?>> HEAT_IMMUNE_ENTITY_TYPE = createEntityTypeTag("heat_immune");

    /**
     * Conventional tag for consumables that are warming (has no effects with Thermoo alone)
     */
    public static final TagKey<Item> CONSUMABLE_WARMING = createItemTag("consumable/warming");

    /**
     * Conventional tag for consumables that are cooling (has no effects with Thermoo alone)
     */
    public static final TagKey<Item> CONSUMABLE_COOLING = createItemTag("consumable/cooling");


    private static TagKey<EntityType<?>> createEntityTypeTag(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Thermoo.location(path));
    }

    private static TagKey<Block> createBlockTag(String path) {
        return TagKey.create(Registries.BLOCK, Thermoo.location(path));
    }

    private static TagKey<Item> createItemTag(String path) {
        return TagKey.create(Registries.ITEM, Thermoo.location(path));
    }

    private ThermooTags() {
    }
}
