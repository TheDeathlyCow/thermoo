/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
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

package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooBuiltInRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Function;

/// Provides the environment parameters (such as temperature and relative humidity) of a position in a world and biome.
public interface EnvironmentProvider {
    Codec<EnvironmentProvider> ELEMENT_CODEC = ThermooBuiltInRegistries.ENVIRONMENT_PROVIDER_TYPE.byNameCodec()
            .dispatch(EnvironmentProvider::codec, Function.identity());

    Codec<Holder<EnvironmentProvider>> HOLDER_CODEC = RegistryFileCodec.create(
            ThermooRegistries.ENVIRONMENT_PROVIDER,
            ELEMENT_CODEC
    );

    /// Builds the current environment parameter components at a point and biome in a level into a builder.
    ///
    /// Only component type keys registered in the
    /// [environment component type registry][ThermooBuiltInRegistries#ENVIRONMENT_COMPONENT_TYPE]. A set of default components
    /// for temperature, relative humidity, and atmospheric pressure are defined in [com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes].
    ///
    /// @param level   The level being queried
    /// @param pos     The position in the world to query
    /// @param biome   The biome at the position in the world
    /// @param builder A component map builder to append to
    void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder);

    /// @return Returns the codec of the provider.
    MapCodec<? extends EnvironmentProvider> codec();
}