package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.predicate.ThermooLootConditionTypes;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ThermooCommonRegisters {
    public static void register(RegisterEvent event) {
        registerTemperatureEffects(event);
        registerEnvironmentProviderTypes(event);
        registerLootConditionTypes(event);
    }

    private static void registerTemperatureEffects(RegisterEvent event) {
        registerTemperatureEffect(event, "empty", TemperatureEffects.EMPTY);
        registerTemperatureEffect(event, "sequence", TemperatureEffects.SEQUENCE);
        registerTemperatureEffect(event, "function", TemperatureEffects.FUNCTION);
        registerTemperatureEffect(event, "mob_effect", TemperatureEffects.MOB_EFFECT);
        registerTemperatureEffect(event, "status_effect", TemperatureEffects.STATUS_EFFECT);
        registerTemperatureEffect(event, "scaling_attribute_modifier", TemperatureEffects.SCALING_ATTRIBUTE_MODIFIER);
        registerTemperatureEffect(event, "attribute_modifier", TemperatureEffects.ATTRIBUTE_MODIFIER);
        registerTemperatureEffect(event, "damage", TemperatureEffects.DAMAGE);
    }

    private static void registerEnvironmentProviderTypes(RegisterEvent event) {
        registerEnvironmentProviderType(event, "constant", EnvironmentProviderTypes.CONSTANT);
        registerEnvironmentProviderType(event, "seasonal/temperate", EnvironmentProviderTypes.TEMPERATE_SEASONAL);
        registerEnvironmentProviderType(event, "seasonal/tropical", EnvironmentProviderTypes.TROPICAL_SEASONAL);
        registerEnvironmentProviderType(event, "modify", EnvironmentProviderTypes.MODIFY);
        registerEnvironmentProviderType(event, "light_threshold", EnvironmentProviderTypes.LIGHT_THRESHOLD);
        registerEnvironmentProviderType(event, "weather_state", EnvironmentProviderTypes.WEATHER_STATE);
        registerEnvironmentProviderType(event, "precipitation_type", EnvironmentProviderTypes.PRECIPITATION_TYPE);
        registerEnvironmentProviderType(event, "temperature_shift", EnvironmentProviderTypes.TEMPERATURE_SHIFT);
    }

    private static void registerLootConditionTypes(RegisterEvent event) {
        registerLootConditionType(event, "temperature", ThermooLootConditionTypes.TEMPERATURE);
        registerLootConditionType(event, "soaked", ThermooLootConditionTypes.SOAKED);
    }

    private static void registerTemperatureEffect(RegisterEvent event, String name, TemperatureEffect<?> temperatureEffect) {
        event.register(ThermooRegistryKeys.TEMPERATURE_EFFECT, Thermoo.id(name), () -> temperatureEffect);
    }

    private static void registerEnvironmentProviderType(RegisterEvent event, String name, EnvironmentProviderType<?> providerType) {
        event.register(ThermooRegistryKeys.ENVIRONMENT_PROVIDER_TYPE, Thermoo.id(name), () -> providerType);
    }

    private static void registerLootConditionType(RegisterEvent event, String name, LootItemConditionType lootConditionType) {
        event.register(Registries.LOOT_CONDITION_TYPE, Thermoo.id(name), () -> lootConditionType);
    }
}
