package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.github.thedeathlycow.thermoo.api.core.v1.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v1.registry.ThermooRegistryKeys;
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
    Codec<EnvironmentProvider> ELEMENT_CODEC = ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE.byNameCodec()
            .dispatch(EnvironmentProvider::codec, Function.identity());

    Codec<Holder<EnvironmentProvider>> HOLDER_CODEC = RegistryFileCodec.create(
            ThermooRegistryKeys.ENVIRONMENT_PROVIDER,
            ELEMENT_CODEC
    );

    /// Builds the current environment parameter components at a point and biome in a level into a builder.
    ///
    /// Only component type keys registered in the
    /// [environment component type registry][ThermooRegistries#ENVIRONMENT_COMPONENT_TYPE]. A set of default components
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