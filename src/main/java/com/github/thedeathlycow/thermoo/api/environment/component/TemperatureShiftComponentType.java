package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;

import java.util.Collection;
import java.util.List;

public class TemperatureShiftComponentType {
    public static final Codec<List<TemperatureShiftComponentType>> CODEC = TemperatureRecord.CODEC
            .xmap(TemperatureShiftComponentType::new, TemperatureShiftComponentType::value)
            .listOf();

    public static final TemperatureShiftComponentType DEFAULT = new TemperatureShiftComponentType(
            new TemperatureRecord(0, TemperatureUnit.KELVIN)
    );
    private final TemperatureRecord temperature;

    public TemperatureShiftComponentType(TemperatureRecord temperature) {
        this.temperature = temperature;
    }

    public TemperatureRecord value() {
        return this.temperature;
    }

    public TemperatureRecord average(Collection<TemperatureRecord> values) {
        int size = values.size();
        if (size == 0) {
            return DEFAULT.value();
        }

        var totalTemperature = new TemperatureRecord(0, TemperatureUnit.KELVIN);
        for (TemperatureRecord value : values) {
            totalTemperature = totalTemperature.add(value);
        }

        return new TemperatureRecord(totalTemperature.value() / size, TemperatureUnit.KELVIN);
    }
}