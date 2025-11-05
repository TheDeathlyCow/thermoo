package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

/**
 * Provides the environment parameters (such as temperature and relative humidity) of a position in a world and biome.
 */
public interface EnvironmentProvider {
    Codec<EnvironmentProvider> ELEMENT_CODEC = ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE.byNameCodec()
            .dispatch("type", EnvironmentProvider::getType, EnvironmentProviderType::codec);

    Codec<Holder<EnvironmentProvider>> ENTRY_CODEC = RegistryFileCodec.create(
            ThermooRegistryKeys.ENVIRONMENT_PROVIDER,
            ELEMENT_CODEC
    );

    /**
     * Builds the current environment parameter components at a point and biome in a world into a reducible builder.
     * <p>
     * The allowed component type keys must be registered in the
     * {@link ThermooRegistries#ENVIRONMENT_COMPONENT_TYPE environment component type registry}. A set of default
     * components for temperature and relative humidity are defined in
     * {@link EnvironmentComponentTypes}.
     *
     * @param world   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder A component map builder to append to
     */
    void buildCurrentComponents(Level world, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder);

    /**
     * @return Returns the type of this provider for dispatch
     */
    EnvironmentProviderType<? extends EnvironmentProvider> getType();
}