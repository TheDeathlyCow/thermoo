package com.github.thedeathlycow.thermoo.impl.test.platform;

import com.github.thedeathlycow.thermoo.impl.platform.ThermooRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ThermooRegistriesImpl implements ThermooRegistries {
    @Override
    public <T> Registry<T> createBuiltinRegistry(ResourceKey<Registry<T>> key) {
        return new MappedRegistry<>(key, Lifecycle.stable());
    }

    @Override
    public <T> void registerDynamicRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        
    }

    @Override
    public <T> void registerSyncedDynamicRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {

    }

    @Override
    public void addAlias(Registry<?> registry, Identifier oldId, Identifier newId) {

    }
}