package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registry;

import java.util.HashMap;
import java.util.Map;

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

    private ThermooRegistries() {

    }

}
