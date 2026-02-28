package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.environment.attribute.ThermooAttributeTypes;
import com.github.thedeathlycow.thermoo.api.environment.attribute.ThermooEnvironmentAttributes;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.predicate.SoakedLootCondition;
import com.github.thedeathlycow.thermoo.api.predicate.TemperatureLootCondition;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectV2;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.AttributeModifierEffectV2;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.DamageEffectV2;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AttributeType;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;


public final class ThermooCommonRegisters {
    public static void registerTemperatureEffects() {
        registerTemperatureEffectType("attribute_modifier", AttributeModifierEffectV2.CODEC);
        registerTemperatureEffectType("damage", DamageEffectV2.CODEC);
//        registerTemperatureEffect("empty", TemperatureEffects.EMPTY);
//        registerTemperatureEffect("sequence", TemperatureEffects.SEQUENCE);
//        registerTemperatureEffect("function", TemperatureEffects.FUNCTION);
//        registerTemperatureEffect("mob_effect", TemperatureEffects.MOB_EFFECT);
//        registerTemperatureEffect("scaling_attribute_modifier", TemperatureEffects.SCALING_ATTRIBUTE_MODIFIER);
//        registerTemperatureEffect("attribute_modifier", TemperatureEffects.ATTRIBUTE_MODIFIER);
//        registerTemperatureEffect("damage", TemperatureEffects.DAMAGE);
//
//        ThermooRegistries.TEMPERATURE_EFFECTS.addAlias(
//                Thermoo.id("status_effect"),
//                Thermoo.id("mob_effect")
//        );
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
        registerEnvironmentProviderType("set_temperature_from_pressure", EnvironmentProviderTypes.SET_TEMPERATURE_FROM_PRESSURE);
        registerEnvironmentProviderType("set_pressure_from_altitude", EnvironmentProviderTypes.SET_PRESSURE_FROM_ALTITUDE);
    }

    public static void registerLootConditionTypes() {
        registerLootConditionType("temperature", TemperatureLootCondition.CODEC);
        registerLootConditionType("soaked", SoakedLootCondition.CODEC);
    }

    public static void registerEnvironmentAttributes() {
        registerAttributeType("temperate_season", ThermooAttributeTypes.TEMPERATE_SEASON);
        registerAttributeType("tropical_season", ThermooAttributeTypes.TROPICAL_SEASON);
        registerAttributeType("temperature", ThermooAttributeTypes.TEMPERATURE);
        registerAttributeType("positive_double", ThermooAttributeTypes.POSITIVE_DOUBLE);
        registerEnvironmentAttribute("gameplay/temperate_season", ThermooEnvironmentAttributes.TEMPERATE_SEASON);
        registerEnvironmentAttribute("gameplay/tropical_season", ThermooEnvironmentAttributes.TROPICAL_SEASON);
        registerEnvironmentAttribute("gameplay/temperate_season_progress", ThermooEnvironmentAttributes.TEMPERATE_SEASON_PROGRESS);
        registerEnvironmentAttribute("gameplay/tropical_season_progress", ThermooEnvironmentAttributes.TROPICAL_SEASON_PROGRESS);
        registerEnvironmentAttribute("gameplay/temperature", ThermooEnvironmentAttributes.TEMPERATURE);
        registerEnvironmentAttribute("gameplay/atmospheric_pressure", ThermooEnvironmentAttributes.ATMOSPHERIC_PRESSURE);
    }

    private static void registerTemperatureEffectType(String name, MapCodec<? extends TemperatureEffectV2> temperatureEffect) {
        Registry.register(ThermooRegistries.TEMPERATURE_EFFECT_TYPE, Thermoo.id(name), temperatureEffect);
    }

    private static void registerEnvironmentProviderType(String name, EnvironmentProviderType<?> providerType) {
        Registry.register(ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE, Thermoo.id(name), providerType);
    }

    private static void registerLootConditionType(String name, MapCodec<? extends LootItemCondition> lootConditionType) {
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, Thermoo.id(name), lootConditionType);
    }

    private static <V> AttributeType<V> registerAttributeType(String name, AttributeType<V> type) {
        return Registry.register(BuiltInRegistries.ATTRIBUTE_TYPE, Thermoo.id(name), type);
    }

    private static <V> EnvironmentAttribute<V> registerEnvironmentAttribute(String name, EnvironmentAttribute<V> attribute) {
        return Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, Thermoo.id(name), attribute);
    }

    private ThermooCommonRegisters() {

    }
}
