package com.github.thedeathlycow.thermoo.impl.environment.attribute;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record DoubleWithAlpha(double value, double alpha) {
    private static final Codec<DoubleWithAlpha> FULL_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.DOUBLE
                                    .fieldOf("value")
                                    .forGetter(DoubleWithAlpha::value),
                            Codec.doubleRange(0.0, 1.0)
                                    .optionalFieldOf("alpha", 1.0)
                                    .forGetter(DoubleWithAlpha::alpha)
                    )
                    .apply(instance, DoubleWithAlpha::new)
    );
    public static final Codec<DoubleWithAlpha> CODEC = Codec.either(Codec.DOUBLE, FULL_CODEC)
            .xmap(
                    either -> either.map(DoubleWithAlpha::new, doubleWithAlpha -> doubleWithAlpha),
                    doubleWithAlpha -> doubleWithAlpha.alpha() == 1.0
                            ? Either.left(doubleWithAlpha.value())
                            : Either.right(doubleWithAlpha)
            );

    public DoubleWithAlpha(double value) {
        this(value, 1.0f);
    }
}