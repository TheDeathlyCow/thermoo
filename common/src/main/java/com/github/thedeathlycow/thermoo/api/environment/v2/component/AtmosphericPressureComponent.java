package com.github.thedeathlycow.thermoo.api.environment.v2.component;

import com.mojang.serialization.Codec;

/**
 * Stores the codec and default value for {@link EnvironmentComponentTypes#ATMOSPHERIC_PRESSURE}
 */
public final class AtmosphericPressureComponent {
    /**
     * A double codec that requires positive values.
     */
    public static final Codec<Double> CODEC = Codec.doubleRange(0.0, Double.MAX_VALUE);
    
    /**
     * The default atmospheric pressure, 1013.25mbar or exactly 1 standard atmosphere.
     */
    public static final double DEFAULT = 1_013.25;

    private AtmosphericPressureComponent() {

    }
}