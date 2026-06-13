package com.github.thedeathlycow.thermoo.impl.platform;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/// A platform independent abstraction for a Dynamic Registries API, based on the equivalent class provided by Fabric
///
/// This is not a stable API!
public interface ThermooDynamicRegistries {
    <T> void register(ResourceKey<? extends Registry<T>> key, Codec<T> codec);

    <T> void registerSynced(ResourceKey<? extends Registry<T>> key, Codec<T> codec);
}