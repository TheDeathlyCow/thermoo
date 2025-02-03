package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class ThermooRegistryKeys {
    /**
     * Key for the temperature effect registry
     * <p>
     * Note that the datapack registry for temperature effects is defined by a {@link com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect}
     *
     * @see TemperatureEffect
     */
    public static final RegistryKey<Registry<TemperatureEffect<?>>> TEMPERATURE_EFFECT = createRegistryKey("temperature_effects");

    /**
     * Key for the environment provider type registry
     *
     * @see EnvironmentProviderType
     */
    public static final RegistryKey<Registry<EnvironmentProviderType<?>>> ENVIRONMENT_PROVIDER_TYPE = createRegistryKey("environment_provider_type");

    /**
     * Key for the environment definition registry
     * <p>
     * This registry is a dynamic registry, with elements defined from a datapack in the folder {@code /thermoo/environment/}
     *
     * @see EnvironmentDefinition
     */
    public static final RegistryKey<Registry<EnvironmentDefinition>> ENVIRONMENT = createRegistryKey("environment");

    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.ofRegistry(Thermoo.id(registryId));
    }

    private ThermooRegistryKeys() {

    }
}
