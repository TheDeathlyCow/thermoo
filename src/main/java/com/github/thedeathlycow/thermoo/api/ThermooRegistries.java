package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ThermooRegistries {

    public static final Registry<TemperatureEffect<?>> TEMPERATURE_EFFECTS =
            FabricRegistryBuilder.createSimple(
                    ThermooRegistries.<TemperatureEffect<?>>castClass(TemperatureEffect.class),
                    Thermoo.id("temperature_effects")
            ).buildAndRegister();

    public static final RegistryKey<Registry<TemperatureEffect<?>>> TEMPERATURE_EFFECT_KEY = createRegistryKey("temperature_effects");

    @SuppressWarnings("unchecked")
    private static <T> Class<T> castClass(Class<?> clazz) {
        return (Class<T>) clazz;
    }

    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.ofRegistry(Thermoo.id(registryId));
    }

}
