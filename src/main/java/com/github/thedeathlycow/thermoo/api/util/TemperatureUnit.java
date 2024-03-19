package com.github.thedeathlycow.thermoo.api.util;

import java.util.function.DoubleUnaryOperator;

/**
 * Defines the basic units of temperature and allows for conversions between them.
 */
public enum TemperatureUnit {

    CELSIUS(
            value -> value,
            celsiusValue -> celsiusValue
    ),
    KELVIN(
            value -> value - 273.15,
            celsiusValue -> celsiusValue + 273.15
    ),
    FAHRENHEIT(
            value -> (value - 32.0) * 5.0 / 9.0,
            celsiusValue -> (5.0 / 9.0) * celsiusValue + 32.0
    ),
    RANKINE(
            value -> FAHRENHEIT.toCelsius(value - 459.67),
            celsiusValue -> FAHRENHEIT.fromCelsius(celsiusValue) - 459.67
    );

    private final DoubleUnaryOperator toCelsius;

    private final DoubleUnaryOperator fromCelsius;

    TemperatureUnit(DoubleUnaryOperator toCelsius, DoubleUnaryOperator fromCelsius) {
        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
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
        double inputCelsius = inputUnit.toCelsius(inputValue);
        return this.fromCelsius(inputCelsius);
    }

}
