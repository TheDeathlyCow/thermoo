package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

/**
 * All tags used by Thermoo. Thermoo by default leaves these tags as empty - even for vanilla entries
 */
public class ThermooTags {

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
     * Blocks that are hot to step on
     */
    public static final TagKey<Block> HOT_FLOOR = createBlockTag("hot_floor");

    private static TagKey<EntityType<?>> createEntityTypeTag(String path) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Thermoo.id(path));
    }

    private static TagKey<Block> createBlockTag(String path) {
        return TagKey.of(RegistryKeys.BLOCK, Thermoo.id(path));
    }

}
