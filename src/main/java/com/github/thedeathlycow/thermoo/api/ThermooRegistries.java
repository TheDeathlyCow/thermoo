package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;

/**
 * Custom registries provided by Thermoo
 */
public final class ThermooRegistries {

    /**
     * Temperature effects
     *
     * @see TemperatureEffect
     */
    public static final Registry<TemperatureEffect<?>> TEMPERATURE_EFFECTS =
            FabricRegistryBuilder.createSimple(
                    ThermooRegistryKeys.TEMPERATURE_EFFECT
            ).buildAndRegister();

    @SuppressWarnings("unchecked")
    private static <T> Class<T> castClass(Class<?> clazz) {
        return (Class<T>) clazz;
    }

    private ThermooRegistries() {

    }

}
