package com.github.thedeathlycow.thermoo.impl.platform;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/// A platform independent abstraction for a registry API, based on the FabricRegistryBuilder and DynamicRegistries
/// classes provided by Fabric
///
/// This is not a stable API!
public interface ThermooRegistries {
    <T> Registry<T> createBuiltinRegistry(ResourceKey<Registry<T>> key);

    <T> void registerDynamicRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> codec);

    <T> void registerSyncedDynamicRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> codec);

    void addAlias(Registry<?> registry, Identifier oldId, Identifier newId);
}