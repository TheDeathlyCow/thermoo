package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.predicate.ThermooLootConditionTypes;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffects;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ThermooCommonRegisters {

    @SuppressWarnings("deprecation")
    public static void registerTemperatureEffects() {
        registerTemperatureEffect("empty", TemperatureEffects.EMPTY);
        registerTemperatureEffect("sequence", TemperatureEffects.SEQUENCE);
        registerTemperatureEffect("function", TemperatureEffects.FUNCTION);
        registerTemperatureEffect("status_effect", TemperatureEffects.STATUS_EFFECT);
        registerTemperatureEffect("scaling_attribute_modifier", TemperatureEffects.SCALING_ATTRIBUTE_MODIFIER);
        registerTemperatureEffect("damage", TemperatureEffects.DAMAGE);
    }

    public static void registerLootConditionTypes() {
        registerLootConditionType("temperature", ThermooLootConditionTypes.TEMPERATURE);
        registerLootConditionType("soaked", ThermooLootConditionTypes.SOAKED);
    }

    private static void registerTemperatureEffect(String name, TemperatureEffect<?> temperatureEffect) {
        Registry.register(ThermooRegistries.TEMPERATURE_EFFECTS, Thermoo.id(name), temperatureEffect);
    }

    private static void registerLootConditionType(String name, LootConditionType lootConditionType) {
        Registry.register(Registries.LOOT_CONDITION_TYPE, Thermoo.id(name), lootConditionType);
    }
}
