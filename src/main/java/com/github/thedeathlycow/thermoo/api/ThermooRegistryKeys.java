package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;

public final class ThermooRegistryKeys {
    /**
     * Key for the temperature effect registry
     * <p>
     * Note that the datapack registry for temperature effects is defined by a {@link com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect}
     *
     * @see TemperatureEffect
     */
    public static final ResourceKey<Registry<TemperatureEffect<?>>> TEMPERATURE_EFFECT = createRegistryKey("temperature_effects");

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
        return ResourceKey.createRegistryKey(Thermoo.location(registryId));
    }

    private ThermooRegistryKeys() {

    }
}
