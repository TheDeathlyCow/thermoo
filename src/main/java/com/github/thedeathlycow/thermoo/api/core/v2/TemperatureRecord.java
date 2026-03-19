package com.github.thedeathlycow.thermoo.api.core.v2;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/// A class to record a temperature value in a particular unit,
public final class TemperatureRecord implements Comparable<TemperatureRecord> {
    /// Codec for a record that is represented as a named tuple of the value and unit.
    ///
    /// ### Usage Example
    ///
    /// Room temperature in Celsius:
    /// ```json
    /// {
    ///     "value": 20.0,
    ///     "unit": "celsius"
    /// }
    /// ```
    ///
    /// Room temperature in Fahrenheit:
    /// ```json
    /// {
    ///     "value": 68.0,
    ///     "unit": "fahrenheit"
    /// }
    /// ```
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

    /// Codec that allows for the value to be stored as a simple double (in which case the value will be in Celsius) or
    /// as an explicit (value, unit) tuple.
    ///
    /// ### Usage Example
    ///
    /// Storing a Celsius value:
    /// ```json
    /// 20.0
    /// ```
    /// ...is equivalent to:
    /// ```json
    /// {
    ///     "value": 20.0,
    ///     "unit": "celsius"
    /// }
    /// ```
    ///
    /// ---
    ///
    /// Using [Codec#fieldOf(java.lang.String)] to add a key, for example:
    ///
    /// ```json
    /// {
    ///     "temperature": 20.0
    /// }
    /// ```
    /// ...is equivalent to:
    /// ```json
    /// {
    ///     "temperature": {
    ///         "value": 20.0,
    ///         "unit": "celsius"
    ///     }
    /// }
    /// ```
    public static final Codec<TemperatureRecord> CODEC = Codec.either(Codec.DOUBLE, UNIT_CODEC)
            .xmap(
                    either -> either.map(TemperatureRecord::new, temperatureRecord -> temperatureRecord),
                    Either::right
            );

    private final double value;
    private final TemperatureUnit unit;

    /// Constructs a record out of a value and a unit
    ///
    /// @param value The value of the record
    /// @param unit  The unit of the record
    public TemperatureRecord(double value, TemperatureUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    /// Constructs a Celsius record out of a value.
    ///
    /// @param value The Celsius value of the record.
    public TemperatureRecord(double value) {
        this(value, TemperatureUnit.CELSIUS);
    }

    /// @return The value of the record.
    public double value() {
        return value;
    }

    /// @return The unit of the record.
    public TemperatureUnit unit() {
        return unit;
    }

    /// Returns the sum of two temperature records.
    ///
    /// This is different from [#add(TemperatureRecord)] in that the `other` is treated as an actual temperature value,
    /// and not a temperature difference. This operation is generally useful for data analysis, e.g., finding the mean
    /// of N temperature records.
    ///
    /// This operation is commutative IF AND ONLY IF the units are the same.
    ///
    /// For example: `70F + 10F = 80F`, `20C + 10K => -243.15C`, `10K + 20C => 303.15K`.
    ///
    /// @param other the other record to sum
    /// @return Returns a new record that is the sum of this record and the other, in the unit of this record.
    /// @see #shift(TemperatureRecord)
    @Contract("_->new")
    public TemperatureRecord sum(TemperatureRecord other) {
        double shift = other.valueInUnit(this.unit());
        return new TemperatureRecord(this.value() + shift, this.unit());
    }

    /// Adds a temperature change from another record to the temperature in this record.
    ///
    /// This is different from the [#sum(TemperatureRecord)] in that the `other` temperature is a temperature
    /// *difference*, not a temperature value.  If you want to be 10 degrees warmer, this is what you want to use.
    ///
    /// This operation is commutative IF AND ONLY IF the units are the same.
    ///
    /// For example: `70F + 10F = 80F`, `20C += 10K => 30C`, `10K += 20C => 30K`.
    ///
    /// @param other the other record to add
    /// @see #sum(TemperatureRecord)
    @Contract("_->new")
    public TemperatureRecord shift(TemperatureRecord other) {
        double shift = this.unit().getAbsoluteUnit().convertTemperature(
                other.value(),
                other.unit().getAbsoluteUnit()
        );
        return new TemperatureRecord(this.value() + shift, this.unit());
    }

    /// @deprecated Renamed to [#shift(TemperatureRecord)] for a clearer distinction in name.
    @Deprecated(since = "10.0.0")
    public TemperatureRecord add(TemperatureRecord other) {
        return this.shift(other);
    }


    /// Converts this record's value into another unit
    ///
    /// @param unit The unit to convert to
    /// @return This record's value in the given unit.
    public double valueInUnit(TemperatureUnit unit) {
        return unit.convertTemperature(this);
    }

    /// Converts this temperature record to another unit.
    ///
    /// @param unit The unit to convert to
    /// @return Returns a new temperature record if the unit is different from this record's unit, returns this record
    /// if the unit is the same as this record's unit
    public TemperatureRecord convertToUnit(TemperatureUnit unit) {
        if (this.unit == unit) {
            return this;
        }

        return new TemperatureRecord(this.valueInUnit(unit), unit);
    }

    /// Checks if this record stores a roughly equivalent temperature value to the one given in the other record.
    ///
    /// The comparison is performed in this record's unit.
    ///
    /// @param other     The other record to compare to
    /// @param tolerance A positive fuzz factor for how much the units are allowed to be. It must be a temperature value
    ///                                                                                      in this record's unit.
    /// @return Returns true if the value of this record is roughly equivalent to the value of the other record
    public boolean equals(TemperatureRecord other, double tolerance) {
        double otherValue = other.valueInUnit(this.unit);
        return Math.abs(this.value - otherValue) <= tolerance;
    }

    /// Checks if this record stores an equivalent temperature value to the one given in the other record.
    ///
    /// @param o The other record to compare to.
    /// @return Returns true if the value of this record is equivalent to the value of the other record, in the unit
    /// of this record.
    /// @implNote The comparison is performed in Celsius
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemperatureRecord that = (TemperatureRecord) o;
        double thisCelsius = this.unit.toCelsius(this.value);
        double otherCelsius = that.unit.toCelsius(that.value);
        return Double.compare(thisCelsius, otherCelsius) == 0;
    }

    /// Computes the hash value of this record's Celsius value
    @Override
    public int hashCode() {
        return Objects.hash(this.unit.toCelsius(this.value), TemperatureUnit.CELSIUS);
    }

    @Override
    public String toString() {
        return "TemperatureRecord{" +
                "value=" + value +
                ", unit=" + unit +
                '}';
    }

    /// A stricter equality method that checks both records are roughly equal in both value and unit.
    ///
    /// @param other     the other record to compare to
    /// @param tolerance A positive fuzz factor for how much the units are allowed to be. It must be a temperature value
    /// @return Returns true if both records have the same unit, and roughly the same value
    public boolean strictEquals(TemperatureRecord other, double tolerance) {
        return other.unit == this.unit && Math.abs(this.value - other.value) <= tolerance;
    }

    /// A stricter equality method that checks both records are equal in both value and unit.
    ///
    /// @param other the other record to compare to
    /// @return Returns true if both records have the same unit, and the same value
    public boolean strictEquals(TemperatureRecord other) {
        return other.unit == this.unit && Double.compare(other.value, this.value) == 0;
    }

    /// Compares another record to this one, in the space of this record's unit. The comparison is based on equivalence,
    /// for example 20.0°C is equivalent to 68°F.
    ///
    /// @param other the record to be compared.
    /// @return The value `0` if this record represents and equivalent temperature to the other record; a negative
    /// value if this record represents a temperature less than the other record; and a positive value if this record
    /// represents a temperature greater than the other record.
    @Override
    public int compareTo(@NotNull TemperatureRecord other) {
        double otherValue = other.valueInUnit(this.unit());
        return Double.compare(this.value, otherValue);
    }
}