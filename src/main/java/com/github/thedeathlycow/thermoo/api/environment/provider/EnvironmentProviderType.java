package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.mojang.serialization.MapCodec;

/**
 * A registry entry object that stores a {@link MapCodec} for a {@link EnvironmentProvider}
 *
 * @param <T> The provider's type
 */
public final class EnvironmentProviderType<T extends EnvironmentProvider> {
    private final MapCodec<T> codec;

    /**
     * Constructs a new provider type with a codec
     *
     * @param codec The codec for the type
     */
    public EnvironmentProviderType(MapCodec<T> codec) {
        this.codec = codec;
    }

    /**
     * @return Returns the codec for this type
     */
    public MapCodec<T> codec() {
        return this.codec;
    }
}