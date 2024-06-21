package com.github.thedeathlycow.thermoo.api;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

/**
 * Helpful codecs used by Thermoo. May be changed between MC versions as equivalent vanilla codecs are added.
 * <p>
 * Exposed in API for the convenience of API users.
 */
@ApiStatus.Experimental
public class ThermooCodecs {

    /**
     * Creates a codec for an Enum. Either uses the enum ordinal or the name, but prefers the ordinal for more efficient
     * storage.
     *
     * @param clazz The class of the enum.
     * @param <E>   The enum type
     * @return Returns a codec for the enum class
     */
    public static <E extends Enum<E>> Codec<E> createEnumCodec(Class<E> clazz) {
        return Codec.either(
                Codec.INT.xmap(
                        ordinal -> clazz.getEnumConstants()[ordinal],
                        Enum::ordinal
                ),
                Codec.STRING.xmap(
                        name -> Enum.valueOf(clazz, name),
                        Enum::name
                )
        ).xmap(
                either -> either.left().orElseGet(() -> either.right().orElseThrow()),
                Either::left
        );
    }

    private ThermooCodecs() {

    }

}
