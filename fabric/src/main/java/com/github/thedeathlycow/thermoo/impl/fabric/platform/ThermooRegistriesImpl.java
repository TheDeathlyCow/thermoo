package com.github.thedeathlycow.thermoo.impl.fabric.platform;

import com.github.thedeathlycow.thermoo.impl.platform.ThermooRegistries;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ThermooRegistriesImpl implements ThermooRegistries {
    @Override
    public <T> Registry<T> createBuiltinRegistry(ResourceKey<Registry<T>> key) {
        return FabricRegistryBuilder.create(key).buildAndRegister();
    }

    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        DynamicRegistries.register(key, codec);
    }

    @Override
    public <T> void registerSynced(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        DynamicRegistries.registerSynced(key, codec);
    }
}