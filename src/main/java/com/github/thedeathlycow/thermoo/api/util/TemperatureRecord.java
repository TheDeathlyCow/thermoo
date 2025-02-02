package com.github.thedeathlycow.thermoo.api.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class TemperatureRecord {
    private final double value;
    private final TemperatureUnit unit;

    public static final Codec<TemperatureRecord> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE
                            .fieldOf("value")
                            .forGetter(TemperatureRecord::value),
                    TemperatureUnit.CODEC
                            .fieldOf("unit")
                            .forGetter(TemperatureRecord::unit)
            ).apply(instance, TemperatureRecord::new)
    );

    public static final Codec<TemperatureRecord> CELSIUS_CODEC = Codec.DOUBLE
            .xmap(TemperatureRecord::new, TemperatureRecord::value);

    public TemperatureRecord(double value, TemperatureUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public TemperatureRecord(double value) {
        this(value, TemperatureUnit.CELSIUS);
    }

    public double value() {
        return value;
    }

    public TemperatureUnit unit() {
        return unit;
    }
}