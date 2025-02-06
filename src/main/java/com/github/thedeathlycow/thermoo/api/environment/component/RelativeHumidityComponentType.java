package com.github.thedeathlycow.thermoo.api.environment.component;

import com.mojang.serialization.Codec;

public final class RelativeHumidityComponentType {
    public static final Codec<Double> CODEC = Codec.doubleRange(0, 1);
    public static final Double DEFAULT = 0.5;

    private RelativeHumidityComponentType() {

    }
}