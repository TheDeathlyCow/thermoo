/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.api.core.v2.registry;

import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureReduction;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;

///  Keys for all registries provided by Thermoo
public final class ThermooRegistries {
    /// Registry key for the [temperature source][TemperatureSource] datapack registry.
    ///
    /// This registry is datapack registry, with elements defined from a datapack in the folder `/thermoo/temperature_status/`
    ///
    /// @see TemperatureSource
    public static final ResourceKey<Registry<TemperatureSource>> TEMPERATURE_SOURCE = createRegistryKey("temperature_source");

    /// Registry key for the [temperature status][TemperatureStatus] datapack registry.
    ///
    /// This registry is datapack registry, with elements defined from a datapack in the folder `/thermoo/temperature_status/`
    ///
    /// @see TemperatureStatus
    public static final ResourceKey<Registry<TemperatureStatus>> TEMPERATURE_STATUS = createRegistryKey("temperature_status");

    /// Registry key for the environment provider registry.
    ///
    /// This registry is a datapack registry, with elements defined from a datapack in the folder `/thermoo/environment_provider/`
    ///
    /// @see EnvironmentProvider
    public static final ResourceKey<Registry<EnvironmentProvider>> ENVIRONMENT_PROVIDER = createRegistryKey("environment_provider");

    /// Registry key for the environment definition registry.
    ///
    /// This registry is datapack registry, with elements defined from a datapack in the folder `/thermoo/environment/`
    ///
    /// @see EnvironmentDefinition
    public static final ResourceKey<Registry<EnvironmentDefinition>> ENVIRONMENT = createRegistryKey("environment");

    /// Registry key for [environment provider codecs][EnvironmentProvider]. Register codecs to this registry in an entry point.
    ///
    /// @see EnvironmentProvider
    /// @see ThermooBuiltInRegistries#ENVIRONMENT_PROVIDER_TYPE
    public static final ResourceKey<Registry<MapCodec<? extends EnvironmentProvider>>> ENVIRONMENT_PROVIDER_TYPE = createRegistryKey("environment_provider_type");

    /// Registry key for [temperature effect codecs][TemperatureEffect]. Register codecs to this registry in an entry point.
    ///
    /// @see TemperatureEffect
    /// @see ThermooBuiltInRegistries#TEMPERATURE_EFFECT_TYPE
    public static final ResourceKey<Registry<MapCodec<? extends TemperatureEffect>>> TEMPERATURE_EFFECT_TYPE = createRegistryKey("temperature_effect_type");

    /// The key for the environment component type registry. Register data component types to this registry in an entry point.
    ///
    /// @see com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes
    /// @see ThermooBuiltInRegistries#ENVIRONMENT_COMPONENT_TYPE
    public static final ResourceKey<Registry<DataComponentType<?>>> ENVIRONMENT_COMPONENT_TYPE = createRegistryKey("environment_component_type");

    /// Registry key for [temperature status codecs][TemperatureReduction]. Register codecs to this registry in an entry point.
    ///
    /// @see TemperatureReduction
    /// @see ThermooBuiltInRegistries#TEMPERATURE_REDUCTION_TYPE
    public static final ResourceKey<Registry<MapCodec<? extends TemperatureReduction>>> TEMPERATURE_REDUCTION_TYPE = createRegistryKey("temperature_reduction_type");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String registryId) {
        return ResourceKey.createRegistryKey(Thermoo.id(registryId));
    }

    private ThermooRegistries() {

    }
}
