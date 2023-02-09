package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ThermooTags {

    public static final TagKey<EntityType<?>> BENEFITS_FROM_COLD = createEntityTypeTag("benefits_from_cold");

    public static final TagKey<EntityType<?>> BENEFITS_FROM_HEAT = createEntityTypeTag("benefits_from_heat");

    public static final TagKey<EntityType<?>> COLD_IMMUNE = createEntityTypeTag("cold_immune");

    public static final TagKey<EntityType<?>> HEAT_IMMUNE = createEntityTypeTag("heat_immune");


    private static TagKey<EntityType<?>> createEntityTypeTag(String path) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, Thermoo.id(path));
    }

}
