package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;

import java.util.Collection;

public class TemperatureRecordComponentType implements MergedComponentType<TemperatureRecord> {
    public static final Codec<TemperatureRecordComponentType> CODEC = TemperatureRecord.CODEC
            .xmap(TemperatureRecordComponentType::new, TemperatureRecordComponentType::value);

    public static final TemperatureRecord ROOM_TEMPERATURE = new TemperatureRecord(20, TemperatureUnit.CELSIUS);

    private final TemperatureRecord temperature;

    public TemperatureRecordComponentType(TemperatureRecord temperature) {
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
            return ROOM_TEMPERATURE;
        }

        var totalTemperature = new TemperatureRecord(0, TemperatureUnit.KELVIN);
        for (TemperatureRecord value : values) {
            totalTemperature = totalTemperature.sum(value);
        }

        return new TemperatureRecord(totalTemperature.value() / size, TemperatureUnit.KELVIN);
    }
}