package com.github.thedeathlycow.thermoo.api.environment.v2.component;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureUnit;
import com.mojang.serialization.Codec;

/**
 * Stores the codec and default value for temperature record components
 */
public final class TemperatureRecordComponent {
    /**
     * The codec of this component is identical to that of {@link TemperatureRecord}.
     */
    public static final Codec<TemperatureRecord> CODEC = TemperatureRecord.CODEC;
    /**
     * The default temperature value, a comfortable 20C / 68F.
     */
    public static final TemperatureRecord DEFAULT = new TemperatureRecord(20, TemperatureUnit.CELSIUS);

    private TemperatureRecordComponent() {

    }
}