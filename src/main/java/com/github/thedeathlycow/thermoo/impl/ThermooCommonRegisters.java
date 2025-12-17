package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.environment.attribute.ThermooAttributeTypes;
import com.github.thedeathlycow.thermoo.api.environment.attribute.ThermooEnvironmentAttributes;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.predicate.ThermooLootConditionTypes;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffects;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AttributeType;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;


public class ThermooCommonRegisters {
    public static void registerTemperatureEffects() {
        registerTemperatureEffect("empty", TemperatureEffects.EMPTY);
        registerTemperatureEffect("sequence", TemperatureEffects.SEQUENCE);
        registerTemperatureEffect("function", TemperatureEffects.FUNCTION);
        registerTemperatureEffect("mob_effect", TemperatureEffects.MOB_EFFECT);
        registerTemperatureEffect("scaling_attribute_modifier", TemperatureEffects.SCALING_ATTRIBUTE_MODIFIER);
        registerTemperatureEffect("attribute_modifier", TemperatureEffects.ATTRIBUTE_MODIFIER);
        registerTemperatureEffect("damage", TemperatureEffects.DAMAGE);

        ThermooRegistries.TEMPERATURE_EFFECTS.addAlias(
                Thermoo.id("status_effect"),
                Thermoo.id("mob_effect")
        );
    }

    public static void registerEnvironmentProviderTypes() {
        registerEnvironmentProviderType("constant", EnvironmentProviderTypes.CONSTANT);
        registerEnvironmentProviderType("seasonal/temperate", EnvironmentProviderTypes.TEMPERATE_SEASONAL);
        registerEnvironmentProviderType("seasonal/tropical", EnvironmentProviderTypes.TROPICAL_SEASONAL);
        registerEnvironmentProviderType("modify", EnvironmentProviderTypes.MODIFY);
        registerEnvironmentProviderType("light_threshold", EnvironmentProviderTypes.LIGHT_THRESHOLD);
        registerEnvironmentProviderType("weather_state", EnvironmentProviderTypes.WEATHER_STATE);
        registerEnvironmentProviderType("precipitation_type", EnvironmentProviderTypes.PRECIPITATION_TYPE);
        registerEnvironmentProviderType("temperature_shift", EnvironmentProviderTypes.TEMPERATURE_SHIFT);
    }

    public static void registerLootConditionTypes() {
        registerLootConditionType("temperature", ThermooLootConditionTypes.TEMPERATURE);
        registerLootConditionType("soaked", ThermooLootConditionTypes.SOAKED);
    }

    public static void registerEnvironmentAttributes() {
        registerAttributeType("temperate_season", ThermooAttributeTypes.TEMPERATE_SEASON);
        registerAttributeType("tropical_season", ThermooAttributeTypes.TROPICAL_SEASON);
        registerAttributeType("temperature", ThermooAttributeTypes.TEMPERATURE);
        registerEnvironmentAttribute("gameplay/temperate_season", ThermooEnvironmentAttributes.TEMPERATE_SEASON);
        registerEnvironmentAttribute("gameplay/tropical_season", ThermooEnvironmentAttributes.TROPICAL_SEASON);
        registerEnvironmentAttribute("gameplay/temperature", ThermooEnvironmentAttributes.TEMPERATURE);
    }

    private static void registerTemperatureEffect(String name, TemperatureEffect<?> temperatureEffect) {
        Registry.register(ThermooRegistries.TEMPERATURE_EFFECTS, Thermoo.id(name), temperatureEffect);
    }

    private static void registerEnvironmentProviderType(String name, EnvironmentProviderType<?> providerType) {
        Registry.register(ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE, Thermoo.id(name), providerType);
    }

    private static void registerLootConditionType(String name, LootItemConditionType lootConditionType) {
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, Thermoo.id(name), lootConditionType);
    }

    private static <V> AttributeType<V> registerAttributeType(String name, AttributeType<V> type) {
        return Registry.register(BuiltInRegistries.ATTRIBUTE_TYPE, Thermoo.id(name), type);
    }

    private static <V> EnvironmentAttribute<V> registerEnvironmentAttribute(String name, EnvironmentAttribute<V> attribute) {
        return Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, Thermoo.id(name), attribute);
    }
}
