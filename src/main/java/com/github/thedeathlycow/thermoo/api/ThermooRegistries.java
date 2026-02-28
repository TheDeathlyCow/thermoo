package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.temperature.effect.v2.TemperatureEffectV2;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;

/**
 * Custom registries provided by Thermoo
 */
public final class ThermooRegistries {

    /**
     * Temperature effects
     *
     * @see TemperatureEffect
     */
    public static final Registry<MapCodec<? extends TemperatureEffectV2>> TEMPERATURE_EFFECT_TYPE =
            FabricRegistryBuilder.create(
                    ThermooRegistryKeys.TEMPERATURE_EFFECT_TYPE
            ).buildAndRegister();

    /**
     * Environment component type registry
     *
     * @see com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes
     */
    public static final Registry<DataComponentType<?>> ENVIRONMENT_COMPONENT_TYPE = FabricRegistryBuilder.create(
            ThermooRegistryKeys.ENVIRONMENT_COMPONENT_TYPE
    ).buildAndRegister();

    /**
     * Environment provider types registry
     *
     * @see EnvironmentProviderType
     */
    public static final Registry<EnvironmentProviderType<?>> ENVIRONMENT_PROVIDER_TYPE =
            FabricRegistryBuilder.create(
                    ThermooRegistryKeys.ENVIRONMENT_PROVIDER_TYPE
            ).buildAndRegister();

    private ThermooRegistries() {

    }

}
