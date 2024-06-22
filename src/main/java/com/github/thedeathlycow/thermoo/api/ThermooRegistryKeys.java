package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class ThermooRegistryKeys {


    public static final RegistryKey<Registry<TemperatureEffect<?>>> TEMPERATURE_EFFECT = createRegistryKey("temperature_effects");

    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.ofRegistry(Thermoo.id(registryId));
    }

    private ThermooRegistryKeys() {

    }
}
