package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponent;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;

/**
 * A {@link ReducibleComponent reducible} temperature record component that adds temperature differences on reduction
 */
public final class TemperatureRecordComponent implements ReducibleComponent<TemperatureRecordComponent> {
    /**
     * The codec of this component has an equivalent format to a {@link TemperatureRecord}.
     */
    public static final Codec<TemperatureRecordComponent> CODEC = TemperatureRecord.CODEC
            .xmap(TemperatureRecordComponent::new, TemperatureRecordComponent::temperature);
    /**
     * The default temperature value, a comfortable 20C / 68F.
     */
    public static final TemperatureRecord ROOM_TEMPERATURE = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
    /**
     * The default value of this component, with a temperature of {@link #ROOM_TEMPERATURE}.
     */
    public static final TemperatureRecordComponent DEFAULT = new TemperatureRecordComponent(ROOM_TEMPERATURE);

    private final TemperatureRecord value;

    /**
     * Creates a new component value from a temperature record
     *
     * @param value The temperature record to create this from
     */
    public TemperatureRecordComponent(TemperatureRecord value) {
        this.value = value;
    }

    /**
     * Shorthand for creating a component with a value and unit
     *
     * @param value The temperature value
     * @param unit  The temperature value's unit
     */
    public TemperatureRecordComponent(double value, TemperatureUnit unit) {
        this(new TemperatureRecord(value, unit));
    }

    /**
     * Shorthand for creating a component with a Celsius value
     *
     * @param value The Celsius temperature value
     */
    public TemperatureRecordComponent(double value) {
        this(new TemperatureRecord(value));
    }

    /**
     * @return Returns the stored temperature record of this component
     */
    public TemperatureRecord temperature() {
        return this.value;
    }

    /**
     * Reduces this temperature component by {@linkplain TemperatureRecord#add(TemperatureRecord) shifting it} with
     * another component. For example {@code 20C.reduceWith(10C) = 30C}
     *
     * @param other The other component to merge into this one
     * @return Return a new component that holds this components temperature shifted by the other's temperature.
     */
    @Override
    public TemperatureRecordComponent reduceWith(TemperatureRecordComponent other) {
        return new TemperatureRecordComponent(this.value.plus(other.value));
    }
}