package com.github.thedeathlycow.thermoo.impl.fabric.platform;

import com.github.thedeathlycow.thermoo.impl.platform.ThermooDynamicRegistries;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ThermooDynamicRegistriesImpl implements ThermooDynamicRegistries {
    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        DynamicRegistries.register(key, codec);
    }

    @Override
    public <T> void registerSynced(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        DynamicRegistries.registerSynced(key, codec);
    }
}