package com.github.thedeathlycow.thermoo.impl;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;

import java.util.Optional;

public class CodecHelper {
    public static <T> Codec<Optional<T>> optionalCodec(Codec<T> baseCodec) {
        return Codec.either(baseCodec, Codec.EMPTY.codec())
                .xmap(
                        either -> either.map(Optional::of, right -> Optional.empty()),
                        optional -> optional.isPresent()
                                ? Either.left(optional.orElseThrow())
                                : Either.right(Unit.INSTANCE)
                );
    }

    private CodecHelper() {

    }
}