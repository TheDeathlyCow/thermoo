package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;

import java.util.Collection;

public class TemperatureDifferenceComponentType implements MergedComponentType<TemperatureRecord> {
    public static final Codec<TemperatureDifferenceComponentType> CODEC = TemperatureRecord.CODEC
            .xmap(TemperatureDifferenceComponentType::new, TemperatureDifferenceComponentType::value);

    public static final TemperatureRecord NO_DIFFERENCE = new TemperatureRecord(0, TemperatureUnit.KELVIN);


    private final TemperatureRecord temperature;

    public TemperatureDifferenceComponentType(TemperatureRecord temperature) {
        this.temperature = temperature;
    }

    @Override
    public TemperatureRecord value() {
        return this.temperature;
    }

    @Override
    public TemperatureRecord merge(Collection<TemperatureRecord> values) {
        int size = values.size();
        if (size == 0) {
            return NO_DIFFERENCE;
        }

        var totalTemperature = new TemperatureRecord(0, TemperatureUnit.KELVIN);
        for (TemperatureRecord value : values) {
            totalTemperature = totalTemperature.add(value);
        }

        return new TemperatureRecord(totalTemperature.value() / size, TemperatureUnit.KELVIN);
    }
}