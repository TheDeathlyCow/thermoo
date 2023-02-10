package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.ThermooAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.registry.Registry;

public class ThermooCommonRegisters {

    public static void registerAttributes() {
        registerAttribute("generic.min_temperature", ThermooAttributes.MIN_TEMPERATURE);
        registerAttribute("generic.max_temperature", ThermooAttributes.MAX_TEMPERATURE);
        registerAttribute("generic.frost_resistance", ThermooAttributes.FROST_RESISTANCE);
        registerAttribute("generic.heat_resistance", ThermooAttributes.HEAT_RESISTANCE);
    }

    private static void registerAttribute(String name, EntityAttribute attribute) {
        Registry.register(Registry.ATTRIBUTE, Thermoo.id(name), attribute);
    }

}
