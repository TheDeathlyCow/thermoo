package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

/**
 * All tags used by Thermoo
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


    private static TagKey<EntityType<?>> createEntityTypeTag(String path) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, Thermoo.id(path));
    }

}
