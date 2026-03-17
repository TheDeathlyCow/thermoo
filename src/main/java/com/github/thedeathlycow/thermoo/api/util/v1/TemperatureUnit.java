package com.github.thedeathlycow.thermoo.api.util.v1;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.function.DoubleUnaryOperator;

/**
 * Defines the basic units of temperature and allows for conversions between them.
 */
public enum TemperatureUnit implements StringRepresentable {

    CELSIUS(
            "C",
            "celsius",
            celsiusValue -> celsiusValue,
            celsiusValue -> celsiusValue,
            1
    ),
    KELVIN(
            "K",
            "kelvin",
            kelvinValue -> kelvinValue - 273.15,
            celsiusValue -> celsiusValue + 273.15,
            1
    ),
    FAHRENHEIT(
            "F",
            "fahrenheit",
            fahrenheitValue -> (fahrenheitValue - 32.0) * 5.0 / 9.0,
            celsiusValue -> (9.0 / 5.0) * celsiusValue + 32.0,
            3
    ),
    RANKINE(
            "R",
            "rankine",
            rankineValue -> FAHRENHEIT.toCelsius(rankineValue - 459.67),
            celsiusValue -> FAHRENHEIT.fromCelsius(celsiusValue) + 459.67,
            3
    );

    public static final Codec<TemperatureUnit> CODEC = StringRepresentable.fromEnum(TemperatureUnit::values);

    private final String unitSymbol;

    private final String name;

    private final DoubleUnaryOperator toCelsius;

    private final DoubleUnaryOperator fromCelsius;

    private final int absoluteUnitIndex;

    TemperatureUnit(String unitSymbol, String name, DoubleUnaryOperator toCelsius, DoubleUnaryOperator fromCelsius, int absoluteUnitIndex) {
        this.unitSymbol = unitSymbol;
        this.name = name;
        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
        this.absoluteUnitIndex = absoluteUnitIndex;
    }

    /**
     * Gets the absolute unit scale for this unit. That is, the unit that has the same temperature step as this unit,
     * but whose origin point (0 degrees) is <a href="https://en.wikipedia.org/wiki/Absolute_zero">absolute zero</a>.
     *
     * @return If this unit is Celsius or Kelvin, returns Kelvin. If this unit is Fahrenheit or Rankine, returns Rankine.
     */
    public TemperatureUnit getAbsoluteUnit() {
        return values()[this.absoluteUnitIndex];
    }

    /**
     * Gets the absolute zero temperature value of this unit
     *
     * @return Returns 0 if this is the absolute unit, or some other negative value if it is not the absolute unit.
     */
    public double getAbsoluteZero() {
        return this.convertTemperature(0, this.getAbsoluteUnit());
    }

    public String getUnitSymbol() {
        return this.unitSymbol;
    }

    /**
     * Converts a temperature value in this unit to Celsius.
     *
     * @param value A temperature value in this unit
     * @return Returns the equivalent temperature value in Celsius
     */
    public double toCelsius(double value) {
        return this.toCelsius.applyAsDouble(value);
    }

    /**
     * Converts a temperature value in Celsius to this unit.
     *
     * @param celsiusValue A temperature value in Celsius
     * @return Returns the equivalent temperature value in this unit
     */
    public double fromCelsius(double celsiusValue) {
        return this.fromCelsius.applyAsDouble(celsiusValue);
    }

    /**
     * Converts a temperature in some other unit to this unit.
     *
     * @param inputValue The input temperature value in the other unit
     * @param inputUnit  The other unit
     * @return Returns the equivalent temperature value in this unit.
     */
    public double convertTemperature(double inputValue, TemperatureUnit inputUnit) {
        if (this == inputUnit) {
            return inputValue;
        }
        double inputCelsius = inputUnit.toCelsius(inputValue);
        return this.fromCelsius(inputCelsius);
    }

    /**
     * Converts a temperature record in some other unit to this unit.
     *
     * @param temperatureRecord The record of the temperature to convert
     * @return Returns the equivalent temperature value in this unit.
     */
    public double convertTemperature(TemperatureRecord temperatureRecord) {
        return this.convertTemperature(temperatureRecord.value(), temperatureRecord.unit());
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
