package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;

public final class TemperatureRecordComponent implements ReducibleComponent<TemperatureRecordComponent> {
    public static final Codec<TemperatureRecordComponent> CODEC = TemperatureRecord.CODEC
            .xmap(TemperatureRecordComponent::new, TemperatureRecordComponent::temperature);
    public static final TemperatureRecord ROOM_TEMPERATURE = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
    public static final TemperatureRecordComponent DEFAULT = new TemperatureRecordComponent(ROOM_TEMPERATURE);

    private final TemperatureRecord value;

    public TemperatureRecordComponent(TemperatureRecord value) {
        this.value = value;
    }

    public TemperatureRecord temperature() {
        return this.value;
    }

    @Override
    public TemperatureRecordComponent mergeWith(TemperatureRecordComponent other) {
        return new TemperatureRecordComponent(this.value.plus(other.value));
    }
}