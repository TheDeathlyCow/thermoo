package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.core.v1.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v1.source.RandomlyDodgeReduction;
import com.github.thedeathlycow.thermoo.api.core.v1.source.ReinforcingAttributeReduction;
import com.github.thedeathlycow.thermoo.api.core.v1.source.ScaledAttributeReduction;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureReduction;
import com.github.thedeathlycow.thermoo.api.entity.v1.predicate.SoakedLootCondition;
import com.github.thedeathlycow.thermoo.api.entity.v1.predicate.TemperatureLootCondition;
import com.github.thedeathlycow.thermoo.api.environment.v2.attribute.ThermooAttributeTypes;
import com.github.thedeathlycow.thermoo.api.environment.v2.attribute.ThermooEnvironmentAttributes;
import com.github.thedeathlycow.thermoo.api.environment.v2.provider.*;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.AttributeModifierEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.DamageEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.FunctionEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.MobEffectEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AttributeType;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;


public final class ThermooCommonRegisters {
    public static void registerTemperatureReductions() {
        registerTemperatureReduction("scaled_attribute", ScaledAttributeReduction.CODEC);
        registerTemperatureReduction("reinforcing_attribute", ReinforcingAttributeReduction.CODEC);
        registerTemperatureReduction("randomly_dodge", RandomlyDodgeReduction.CODEC);
    }

    public static void registerTemperatureEffects() {
        registerTemperatureEffectType("attribute_modifier", AttributeModifierEffect.CODEC);
        registerTemperatureEffectType("damage", DamageEffect.CODEC);
        registerTemperatureEffectType("function", FunctionEffect.CODEC);
        registerTemperatureEffectType("mob_effect", MobEffectEffect.CODEC);

        ThermooRegistries.TEMPERATURE_EFFECT_TYPE.addAlias(
                Thermoo.id("status_effect"),
                Thermoo.id("mob_effect")
        );
    }

    public static void registerEnvironmentProviderTypes() {
        registerEnvironmentProviderType("seasonal/temperate", TemperateSeasonSelector.CODEC);
        registerEnvironmentProviderType("seasonal/tropical", TropicalSeasonSelector.CODEC);
        registerEnvironmentProviderType("light_threshold", LightThresholdSelector.CODEC);
        registerEnvironmentProviderType("weather_state", WeatherStateSelector.CODEC);
        registerEnvironmentProviderType("precipitation_type", PrecipitationTypeSelector.CODEC);

        registerEnvironmentProviderType("modify", ModifyProvider.CODEC);
        registerEnvironmentProviderType("constant", ConstantProvider.CODEC);
        registerEnvironmentProviderType("shift_temperature", ShiftTemperatureProvider.CODEC);
        registerEnvironmentProviderType("set_temperature_from_pressure", SetTemperatureFromPressureProvider.CODEC);
        registerEnvironmentProviderType("set_pressure_from_altitude", SetPressureFromAltitudeProvider.CODEC);

        ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE.addAlias(
                Thermoo.id("temperature_shift"),
                Thermoo.id("shift_temperature")
        );
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

    private static void registerTemperatureReduction(String name, MapCodec<? extends TemperatureReduction> reduction) {
        Registry.register(ThermooRegistries.TEMPERATURE_REDUCTION_TYPE, Thermoo.id(name), reduction);
    }

    private static void registerTemperatureEffectType(String name, MapCodec<? extends TemperatureEffect> temperatureEffect) {
        Registry.register(ThermooRegistries.TEMPERATURE_EFFECT_TYPE, Thermoo.id(name), temperatureEffect);
    }

    private static void registerEnvironmentProviderType(String name, MapCodec<? extends EnvironmentProvider> codec) {
        Registry.register(ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE, Thermoo.id(name), codec);
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
