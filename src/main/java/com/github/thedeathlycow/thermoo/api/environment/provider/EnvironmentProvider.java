package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Provides the environment parameters (such as temperature and relative humidity) of a position in a world and biome.
 */
public interface EnvironmentProvider {
    Codec<EnvironmentProvider> ELEMENT_CODEC = ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE.getCodec()
            .dispatch("type", EnvironmentProvider::getType, EnvironmentProviderType::codec);

    Codec<RegistryEntry<EnvironmentProvider>> ENTRY_CODEC = RegistryElementCodec.of(
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
    void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ComponentMap.Builder builder);

    /**
     * @return Returns the type of this provider for dispatch
     */
    EnvironmentProviderType<? extends EnvironmentProvider> getType();
}