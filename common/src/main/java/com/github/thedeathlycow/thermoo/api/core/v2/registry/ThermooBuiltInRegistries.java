package com.github.thedeathlycow.thermoo.api.core.v2.registry;

import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureReduction;
import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;

/// Custom builtin registries provided by Thermoo
public final class ThermooBuiltInRegistries {
    /// Registry for [temperature reduction codecs][TemperatureReduction].
    ///
    /// @see TemperatureReduction
    /// @see ThermooRegistries#TEMPERATURE_REDUCTION_TYPE
    public static final Registry<MapCodec<? extends TemperatureReduction>> TEMPERATURE_REDUCTION_TYPE = ThermooServices.REGISTRIES.createBuiltinRegistry(
            ThermooRegistries.TEMPERATURE_REDUCTION_TYPE
    );

    /// Registry for [temperature effect codecs][TemperatureEffect].
    ///
    /// @see TemperatureEffect
    /// @see ThermooRegistries#TEMPERATURE_EFFECT_TYPE
    public static final Registry<MapCodec<? extends TemperatureEffect>> TEMPERATURE_EFFECT_TYPE = ThermooServices.REGISTRIES.createBuiltinRegistry(
            ThermooRegistries.TEMPERATURE_EFFECT_TYPE
    );

    /// Registry for [environment components][com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes].
    ///
    /// @see com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes
    /// @see ThermooRegistries#ENVIRONMENT_COMPONENT_TYPE
    public static final Registry<DataComponentType<?>> ENVIRONMENT_COMPONENT_TYPE = ThermooServices.REGISTRIES.createBuiltinRegistry(
            ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE
    );

    /// Registry for [environment provider codecs][EnvironmentProvider].
    ///
    /// @see TemperatureEffect
    /// @see ThermooRegistries#ENVIRONMENT_PROVIDER_TYPE
    public static final Registry<MapCodec<? extends EnvironmentProvider>> ENVIRONMENT_PROVIDER_TYPE = ThermooServices.REGISTRIES.createBuiltinRegistry(
            ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE
    );

    private ThermooBuiltInRegistries() {

    }
}
