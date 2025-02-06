package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;

public final class TemperatureRecordComponent implements ReducibleComponent<TemperatureRecordComponent, TemperatureRecord> {
    public static final Codec<TemperatureRecordComponent> CODEC = TemperatureRecord.CODEC
            .xmap(TemperatureRecordComponent::new, TemperatureRecordComponent::value);
    public static final TemperatureRecord ROOM_TEMPERATURE = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
    public static final TemperatureRecordComponent DEFAULT = new TemperatureRecordComponent(ROOM_TEMPERATURE);

    private final TemperatureRecord value;

    public TemperatureRecordComponent(TemperatureRecord value) {
        this.value = value;
    }

    public TemperatureRecord value() {
        return this.value;
    }

    @Override
    public TemperatureRecordComponent mergeWith(ReducibleComponent<TemperatureRecordComponent, TemperatureRecord> other) {
        return new TemperatureRecordComponent(this.value.plus(other.value()));
    }
}