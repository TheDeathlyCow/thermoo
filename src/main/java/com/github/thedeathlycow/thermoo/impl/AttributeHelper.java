package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;

public class AttributeHelper {

    public static final List<IdAttributePair> THERMOO_ATTRIBUTES = List.of(
            new IdAttributePair(Thermoo.id("base_min_temperature"), ThermooAttributes.MIN_TEMPERATURE),
            new IdAttributePair(Thermoo.id("base_max_temperature"), ThermooAttributes.MAX_TEMPERATURE),
            new IdAttributePair(Thermoo.id("base_heat_resistance"), ThermooAttributes.HEAT_RESISTANCE),
            new IdAttributePair(Thermoo.id("base_frost_resistance"), ThermooAttributes.FROST_RESISTANCE)
    );

    public record IdAttributePair(Identifier id, RegistryEntry<EntityAttribute> attribute) {

    }

    private AttributeHelper() {

    }

}
