package com.github.thedeathlycow.thermoo.api.util;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class TemperatureUnitTest {
    @ParameterizedTest
    @CsvSource(
            value = {
                    "0.0, 32.0",
                    "100.0, 212.0",
                    "-40.0, -40.0",
                    "-273.15, -459.67",
                    "20.0, 68.0"
            }
    )
    void celsius_to_fahrenheit(double celsius, double expectedFahrenheit) {
        double fahrenheit = TemperatureUnit.FAHRENHEIT.convertTemperature(celsius, TemperatureUnit.CELSIUS);
        Assertions.assertEquals(expectedFahrenheit, fahrenheit, 1e-2);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "32.0, 0.0",
                    "212.0, 100.0",
                    "-40.0, -40.0",
                    "-459.67, -273.15",
                    "68.0, 20.0"
            }
    )
    void fahrenheit_to_celsius(double fahrenheit, double expectedCelsius) {
        double celsius = TemperatureUnit.CELSIUS.convertTemperature(fahrenheit, TemperatureUnit.FAHRENHEIT);
        Assertions.assertEquals(expectedCelsius, celsius, 1e-2);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "0.0, 273.15",
                    "100.0, 373.15",
                    "-273.15, 0.0",
                    "-100.0, 173.15",
                    "20.0, 293.15"
            }
    )
    void celsius_to_kelvin(double celsius, double expectedKelvin) {
        double kelvin = TemperatureUnit.KELVIN.convertTemperature(celsius, TemperatureUnit.CELSIUS);
        Assertions.assertEquals(expectedKelvin, kelvin, 1e-2);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "273.15, 0.0",
                    "373.15, 100.0",
                    "0.0, -273.15",
                    "173.15, -100.0",
                    "293.15, 20.0"
            }
    )
    void kelvin_to_celsius(double kelvin, double expectedCelsius) {
        double celsius = TemperatureUnit.CELSIUS.convertTemperature(kelvin, TemperatureUnit.KELVIN);
        Assertions.assertEquals(expectedCelsius, celsius, 1e-2);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "0.0, -273.15",
                    "491.67, 0.0",
                    "671.67, 100.0",
                    "459.67, -17.78",
                    "527.67, 20.0"
            }
    )
    void rankine_to_celsius(double rankine, double expectedCelsius) {
        double celsius = TemperatureUnit.CELSIUS.convertTemperature(rankine, TemperatureUnit.RANKINE);
        Assertions.assertEquals(expectedCelsius, celsius, 1e-2);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "-273.15, 0.0",
                    "0.0, 491.67",
                    "100.0, 671.67",
                    "-17.78, 459.67",
                    "20.0, 527.67"
            }
    )
    void celsius_to_rankine(double celsius, double expectedRankine) {
        double rankine = TemperatureUnit.RANKINE.convertTemperature(celsius, TemperatureUnit.CELSIUS);
        Assertions.assertEquals(expectedRankine, rankine, 1e-2);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "491.67, 32.0",
                    "671.67, 212.0",
                    "459.67, 0.0",
                    "0.0, -459.67",
                    "527.67, 68.0"
            }
    )
    void rankine_to_fahrenheit(double rankine, double expectedFahrenheit) {
        double fahrenheit = TemperatureUnit.FAHRENHEIT.convertTemperature(rankine, TemperatureUnit.RANKINE);
        Assertions.assertEquals(expectedFahrenheit, fahrenheit, 1e-2);
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "32.0, 491.67",
                    "212.0, 671.67",
                    "0.0, 459.67",
                    "-459.67, 0.0",
                    "68.0, 527.67"
            }
    )
    void fahrenheit_to_rankine(double fahrenheit, double expectedRankine) {
        double rankine = TemperatureUnit.RANKINE.convertTemperature(fahrenheit, TemperatureUnit.FAHRENHEIT);
        Assertions.assertEquals(expectedRankine, rankine, 1e-2);
    }

    @ParameterizedTest
    @EnumSource(value = TemperatureUnit.class, names = {"CELSIUS", "KELVIN"})
    void metricAbsoluteUnitIsKelvin(TemperatureUnit unit) {
        TemperatureUnit absoluteUnit = unit.getAbsoluteUnit();
        Assertions.assertEquals(TemperatureUnit.KELVIN, absoluteUnit);
    }

    @ParameterizedTest
    @EnumSource(value = TemperatureUnit.class, names = {"FAHRENHEIT", "RANKINE"})
    void imperialAbsoluteUnitIsRankine(TemperatureUnit unit) {
        TemperatureUnit absoluteUnit = unit.getAbsoluteUnit();
        Assertions.assertEquals(TemperatureUnit.RANKINE, absoluteUnit);
    }

    @Test
    void kelvinAbsoluteZeroIsZero() {
        Assertions.assertEquals(0.0, TemperatureUnit.KELVIN.getAbsoluteZero(), 1e-3);
    }

    @Test
    void rankineAbsoluteZeroIsZero() {
        Assertions.assertEquals(0.0, TemperatureUnit.RANKINE.getAbsoluteZero(), 1e-3);
    }

    @Test
    void celsiusAbsoluteZeroIsCorrect() {
        Assertions.assertEquals(-273.15, TemperatureUnit.CELSIUS.getAbsoluteZero(), 1e-3);
    }

    @Test
    void fahrenheitAbsoluteZeroIsCorrect() {
        Assertions.assertEquals(-459.67, TemperatureUnit.FAHRENHEIT.getAbsoluteZero(), 1e-3);
    }
}
