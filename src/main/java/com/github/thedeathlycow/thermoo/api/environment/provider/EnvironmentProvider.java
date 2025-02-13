package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Provides the temperature and relative humidity of a position in a biome.
 */
public interface EnvironmentProvider {
    Codec<EnvironmentProvider> ELEMENT_CODEC = ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE.getCodec()
            .dispatch("type", EnvironmentProvider::getType, EnvironmentProviderType::codec);

    Codec<RegistryEntry<EnvironmentProvider>> ENTRY_CODEC = RegistryElementCodec.of(
            ThermooRegistryKeys.ENVIRONMENT_PROVIDER,
            ELEMENT_CODEC
    );

    /**
     * Queries the current environment parameter components at a point and biome in a world.
     * <p>
     * The allowed component type keys must be registered in the
     * {@link ThermooRegistries#ENVIRONMENT_COMPONENT_TYPE environment component type registry}. A set of default
     * components for temperature and relative humidity are defined in
     * {@link EnvironmentComponentTypes}.
     *
     * @param world The world/level being queried
     * @param pos   The position in the world to query
     * @param biome The biome at the position in the world
     * @return Returns a component map of the current world position.
     */
    MergedComponentMap findCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome);

    /**
     * @return Returns the type of this provider for dispatch
     */
    EnvironmentProviderType<?> getType();
}