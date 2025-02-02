package com.github.thedeathlycow.thermoo.api.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class TemperatureRecord {
    public static final Codec<TemperatureRecord> UNIT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE
                            .fieldOf("value")
                            .forGetter(TemperatureRecord::value),
                    TemperatureUnit.CODEC
                            .optionalFieldOf("unit", TemperatureUnit.CELSIUS)
                            .forGetter(TemperatureRecord::unit)
            ).apply(instance, TemperatureRecord::new)
    );

    public static final Codec<TemperatureRecord> CODEC = Codec.either(Codec.DOUBLE, UNIT_CODEC)
            .xmap(
                    either -> either.map(TemperatureRecord::new, temperatureRecord -> temperatureRecord),
                    Either::right
            );

    private final double value;
    private final TemperatureUnit unit;

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