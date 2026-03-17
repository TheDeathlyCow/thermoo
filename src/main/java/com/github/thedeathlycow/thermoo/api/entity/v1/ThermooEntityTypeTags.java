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