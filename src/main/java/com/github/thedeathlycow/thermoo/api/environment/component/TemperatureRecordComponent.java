package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;

public final class TemperatureRecordComponent {
    public static final Codec<TemperatureRecordComponent> CODEC = TemperatureRecord.CODEC
            .xmap(TemperatureRecordComponent::new, TemperatureRecordComponent::temperatureRecord);
    public static final TemperatureRecord ROOM_TEMPERATURE = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
    public static final TemperatureRecordComponent DEFAULT = new TemperatureRecordComponent(ROOM_TEMPERATURE);

    private final TemperatureRecord temperatureRecord;

    public TemperatureRecordComponent(TemperatureRecord temperatureRecord) {
        this.temperatureRecord = temperatureRecord;
    }

    public TemperatureRecord temperatureRecord() {
        return this.temperatureRecord;
    }
}