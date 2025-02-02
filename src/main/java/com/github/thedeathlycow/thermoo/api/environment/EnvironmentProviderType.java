package com.github.thedeathlycow.thermoo.api.environment;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;

public final class EnvironmentProviderType<T extends EnvironmentProvider> {
    private final MapCodec<T> codec;
    private final RegistryEntryList<Biome> biomes;

    public EnvironmentProviderType(MapCodec<T> codec, RegistryEntryList<Biome> biomes) {
        this.codec = codec;
        this.biomes = biomes;
    }

    public MapCodec<T> codec() {
        return this.codec;
    }

    public RegistryEntryList<Biome> biomes() {
        return this.biomes;
    }
}