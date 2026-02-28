package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectV2;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;

public final class ThermooRegistryKeys {
    /**
     * Registry key for the {@linkplain TemperatureStatus temperature status} datapack registry.
     *
     * @see TemperatureStatus
     */
    public static final ResourceKey<Registry<TemperatureStatus>> TEMPERATURE_STATUS = createRegistryKey("temperature_status");

    /**
     * Registry key for the {@linkplain TemperatureEffectV2 temperature effect} datapack registry.
     *
     * @see TemperatureEffectV2
     */
    public static final ResourceKey<Registry<TemperatureEffectV2>> TEMPERATURE_EFFECT = createRegistryKey("temperature_effect");


    /**
     * Registry key for {@linkplain TemperatureEffectV2 temperature effect codecs}. Register codecs to this registry
     * an entry point.
     *
     * @see TemperatureEffectV2
     * @see ThermooRegistries#TEMPERATURE_EFFECT_TYPE
     */
    public static final ResourceKey<Registry<MapCodec<? extends TemperatureEffectV2>>> TEMPERATURE_EFFECT_TYPE = createRegistryKey("temperature_effect_type");

    /**
     * The key for the environment component type registry
     *
     * @see com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes
     */
    public static final ResourceKey<Registry<DataComponentType<?>>> ENVIRONMENT_COMPONENT_TYPE = createRegistryKey("environment_component_type");

    /**
     * Key for the environment provider type registry
     *
     * @see EnvironmentProviderType
     */
    public static final ResourceKey<Registry<EnvironmentProviderType<?>>> ENVIRONMENT_PROVIDER_TYPE = createRegistryKey("environment_provider_type");

    /**
     * Key for the environment provider registry.
     * <p>
     * This registry is a dynamic registry, with elements defined from a datapack in the folder {@code /thermoo/environment_provider/}
     *
     * @see EnvironmentProvider
     */
    public static final ResourceKey<Registry<EnvironmentProvider>> ENVIRONMENT_PROVIDER = createRegistryKey("environment_provider");

    /**
     * Key for the environment definition registry
     * <p>
     * This registry is a dynamic registry, with elements defined from a datapack in the folder {@code /thermoo/environment/}
     *
     * @see EnvironmentDefinition
     */
    public static final ResourceKey<Registry<EnvironmentDefinition>> ENVIRONMENT = createRegistryKey("environment");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String registryId) {
        return ResourceKey.createRegistryKey(Thermoo.id(registryId));
    }

    private ThermooRegistryKeys() {

    }
}
