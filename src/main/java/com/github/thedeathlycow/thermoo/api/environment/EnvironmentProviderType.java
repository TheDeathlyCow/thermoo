package com.github.thedeathlycow.thermoo.api.environment;

import com.mojang.serialization.MapCodec;

public final class EnvironmentProviderType<T extends EnvironmentProvider> {
    private final MapCodec<T> codec;

    public EnvironmentProviderType(MapCodec<T> codec) {
        this.codec = codec;
    }

    public MapCodec<T> codec() {
        return this.codec;
    }
}