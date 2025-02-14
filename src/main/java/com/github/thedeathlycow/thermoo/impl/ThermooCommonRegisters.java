package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.predicate.ThermooLootConditionTypes;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffects;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ThermooCommonRegisters {
    public static void registerTemperatureEffects() {
        registerTemperatureEffect("empty", TemperatureEffects.EMPTY);
        registerTemperatureEffect("sequence", TemperatureEffects.SEQUENCE);
        registerTemperatureEffect("function", TemperatureEffects.FUNCTION);
        registerTemperatureEffect("status_effect", TemperatureEffects.STATUS_EFFECT);
        registerTemperatureEffect("scaling_attribute_modifier", TemperatureEffects.SCALING_ATTRIBUTE_MODIFIER);
        registerTemperatureEffect("attribute_modifier", TemperatureEffects.ATTRIBUTE_MODIFIER);
        registerTemperatureEffect("damage", TemperatureEffects.DAMAGE);
    }

    public static void registerEnvironmentProviderTypes() {
        registerEnvironmentProviderType("reduce_constant", EnvironmentProviderTypes.REDUCE_CONSTANT);
        registerEnvironmentProviderType("replace_constant", EnvironmentProviderTypes.REPLACE_CONSTANT);
        registerEnvironmentProviderType("seasonal/temperate", EnvironmentProviderTypes.TEMPERATE_SEASONAL);
        registerEnvironmentProviderType("seasonal/tropical", EnvironmentProviderTypes.TROPICAL_SEASONAL);
        registerEnvironmentProviderType("reduce_sequence", EnvironmentProviderTypes.REDUCE_SEQUENCE);
        registerEnvironmentProviderType("light_threshold", EnvironmentProviderTypes.LIGHT_THRESHOLD);
        registerEnvironmentProviderType("precipitation_type", EnvironmentProviderTypes.PRECIPITATION_TYPE);
    }

    public static void registerLootConditionTypes() {
        registerLootConditionType("temperature", ThermooLootConditionTypes.TEMPERATURE);
        registerLootConditionType("soaked", ThermooLootConditionTypes.SOAKED);
    }

    private static void registerTemperatureEffect(String name, TemperatureEffect<?> temperatureEffect) {
        Registry.register(ThermooRegistries.TEMPERATURE_EFFECTS, Thermoo.id(name), temperatureEffect);
    }

    private static void registerEnvironmentProviderType(String name, EnvironmentProviderType<?> providerType) {
        Registry.register(ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE, Thermoo.id(name), providerType);
    }

    private static void registerLootConditionType(String name, LootConditionType lootConditionType) {
        Registry.register(Registries.LOOT_CONDITION_TYPE, Thermoo.id(name), lootConditionType);
    }
}
