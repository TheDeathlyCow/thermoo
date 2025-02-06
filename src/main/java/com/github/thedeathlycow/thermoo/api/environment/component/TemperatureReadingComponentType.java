package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;

public final class TemperatureReadingComponentType {
    public static final Codec<TemperatureRecord> CODEC = TemperatureRecord.CODEC;
    public static final TemperatureRecord DEFAULT = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
    private TemperatureReadingComponentType() {

    }
}