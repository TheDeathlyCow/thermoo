package com.github.thedeathlycow.thermoo.api.environment.component;

import com.mojang.serialization.Codec;

public final class RelativeHumidityComponent {
    public static final Codec<Double> CODEC = Codec.doubleRange(0, 1);
    public static final Double DEFAULT = 0.5;

    private RelativeHumidityComponent() {

    }
}