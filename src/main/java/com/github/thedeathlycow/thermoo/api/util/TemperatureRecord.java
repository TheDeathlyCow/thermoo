package com.github.thedeathlycow.thermoo.api.util;

public final class TemperatureRecord {
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