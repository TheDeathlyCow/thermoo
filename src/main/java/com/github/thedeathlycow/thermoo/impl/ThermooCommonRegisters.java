package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffects;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ThermooCommonRegisters {

    @SuppressWarnings("deprecation")
    public static void registerTemperatureEffects() {
        registerTemperatureEffect("empty", TemperatureEffects.EMPTY);
        registerTemperatureEffect("status_effect", TemperatureEffects.STATUS_EFFECT);
        registerTemperatureEffect("scaling_attribute_modifier", TemperatureEffects.SCALING_ATTRIBUTE_MODIFIER);
        registerTemperatureEffect("freeze_damage_legacy", TemperatureEffects.FREEZE_DAMAGE_LEGACY);
    }

    public static void registerAttributes() {
        registerAttribute("generic.min_temperature", ThermooAttributes.MIN_TEMPERATURE);
        registerAttribute("generic.max_temperature", ThermooAttributes.MAX_TEMPERATURE);
        registerAttribute("generic.frost_resistance", ThermooAttributes.FROST_RESISTANCE);
        registerAttribute("generic.heat_resistance", ThermooAttributes.HEAT_RESISTANCE);
    }

    private static void registerAttribute(String name, EntityAttribute attribute) {
        Registry.register(Registries.ATTRIBUTE, Thermoo.id(name), attribute);
    }

    private static void registerTemperatureEffect(String name, TemperatureEffect<?> temperatureEffect) {
        Registry.register(ThermooRegistries.TEMPERATURE_EFFECTS, Thermoo.id(name), temperatureEffect);
    }
}
