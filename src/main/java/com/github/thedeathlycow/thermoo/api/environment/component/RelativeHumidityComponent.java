package com.github.thedeathlycow.thermoo.api.environment.component;

import com.mojang.serialization.Codec;

/**
 * Stores the codec and default value for {@link EnvironmentComponentTypes#RELATIVE_HUMIDITY}
 */
public final class RelativeHumidityComponent {
    /**
     * A double codec that restricts values to a 0-1 percentage scale
     */
    public static final Codec<Double> CODEC = Codec.doubleRange(0, 1);
    /**
     * The default relative humidity, a comfortable 50%
     */
    public static final double DEFAULT = 0.5;

    private RelativeHumidityComponent() {

    }
}