package com.github.thedeathlycow.thermoo.api.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;

/**
 * A class to record a temperature value in a particular unit.
 */
public final class TemperatureRecord implements Comparable<TemperatureRecord> {
    /**
     * Codec for a record that is represented as a named tuple of the value and unit.
     * <p>
     * <h3>Example format</h3>
     * <p>
     * Room temperature in Celsius:
     * <pre>{@code
     * {
     *     "value": 20.0,
     *     "unit": "celsius"
     * }
     * }</pre>
     * <p>
     * Room temperature in Fahrenheit:
     * <pre>{@code
     * {
     *     "value": 68.0,
     *     "unit": "fahrenheit"
     * }
     * }</pre>
     */
    public static final Codec<TemperatureRecord> UNIT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE
                            .fieldOf("value")
                            .forGetter(TemperatureRecord::value),
                    TemperatureUnit.CODEC
                            .fieldOf("unit")
                            .forGetter(TemperatureRecord::unit)
            ).apply(instance, TemperatureRecord::new)
    );

    /**
     * Codec that allows for the value to be stored as a simple double (in which case the value will be in Celsius) or as
     * an explicit value, unit tuple.
     *
     * <h3>Example format</h3>
     * Storing a Celsius value:
     * <pre>{@code
     * 20.0
     * }</pre>
     * <p>
     * Is equivalent to:
     * <pre>{@code
     * {
     *     "value": 20.0,
     *     "unit": "celsius"
     * }
     * }</pre>
     * <p>
     * Using {@link Codec#fieldOf(String)} to add a key, for example:
     * <pre>{@code
     * "temperature": 0.0
     * }</pre>
     * <p>
     * Is equivalent to
     * <pre>{@code
     * "temperature": {
     *     "value": 0.0,
     *     "unit": "celsius"
     * }
     * }</pre>
     */
    public static final Codec<TemperatureRecord> CODEC = Codec.either(Codec.DOUBLE, UNIT_CODEC)
            .xmap(
                    either -> either.map(TemperatureRecord::new, temperatureRecord -> temperatureRecord),
                    Either::right
            );

    private final double value;
    private final TemperatureUnit unit;

    /**
     * Constructs a record out of a value and a unit
     *
     * @param value The value of the record
     * @param unit  The unit of the record
     */
    public TemperatureRecord(double value, TemperatureUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    /**
     * Constructs a Celsius record out of a value.
     *
     * @param value The Celsius value of the record.
     */
    public TemperatureRecord(double value) {
        this(value, TemperatureUnit.CELSIUS);
    }

    /**
     * @return The value of the record
     */
    public double value() {
        return value;
    }

    /**
     * @return The unit of the record
     */
    public TemperatureUnit unit() {
        return unit;
    }

    /**
     * Adds two records together
     *
     * @param other the other record to add
     * @return Returns a new record that is the sum of the two records in this record's unit
     */
    @Contract("_->new")
    public TemperatureRecord add(TemperatureRecord other) {
        double otherValue = other.valueInUnit(this.unit());
        return new TemperatureRecord(this.value() + otherValue, this.unit());
    }

    /**
     * Converts this record's value into another unit
     *
     * @param unit The unit to convert to
     * @return This records value in the given unit
     */
    public double valueInUnit(TemperatureUnit unit) {
        return unit.convertTemperature(this);
    }

    /**
     * Checks if this record stores an equivalent temperature value to the one given in the other record.
     * <p>
     * The comparison is performed in the unit of this record.
     *
     * @param other The other record to compare to.
     * @return Returns true if the value of this record is strictly equal to the value of the other record, in the unit
     * of this record.
     * @see #isEquivalent(TemperatureRecord, double)
     */
    public boolean isEquivalent(TemperatureRecord other) {
        return this.compareTo(other) == 0;
    }

    /**
     * Checks if this record stores a roughly equivalent temperature value to the one given in the other record.
     * <p>
     * The comparison is performed in the unit of this record.
     *
     * @param other     The other record to compare to.
     * @param tolerance A positive fuzz factor for how much the units are allowed to be. It must be a temperature value
     *                  in this record's unit.
     * @return Returns true if the value of this record is roughly equal to the value of the other record, in the unit
     * of this record.
     */
    public boolean isEquivalent(TemperatureRecord other, double tolerance) {
        double otherValue = other.valueInUnit(this.unit());
        return Math.abs(this.value() - otherValue) <= tolerance;
    }

    /**
     * Checks that two temperature records are the same, both in unit and value.
     *
     * @param o The other record to compare to
     * @return Returns true if the other record has the same value and unit as this one.
     * @see #isEquivalent(TemperatureRecord)
     * @see #isEquivalent(TemperatureRecord, double)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemperatureRecord that = (TemperatureRecord) o;
        return Double.compare(value, that.value) == 0 && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    /**
     * Compares another record to this one, in the space of this record's unit. The comparison is based on equivalence,
     * for example 20.0°C is equivalent to 68°F.
     *
     * @param other the record to be compared.
     * @return The value {@code 0} if this record represents and equivalent temperature to the other record; a negative
     * value if this record represents a temperature less than the other record; and a positive value if this record
     * represents a temperature greater than the other record.
     */
    @Override
    public int compareTo(@NotNull TemperatureRecord other) {
        double otherValue = other.valueInUnit(this.unit());
        return Double.compare(this.value(), otherValue);
    }
}