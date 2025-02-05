package com.github.thedeathlycow.thermoo.api.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TemperatureRecordTest {
    @Test
    void addTwoCelsius() {
        var roomTemperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        var add = new TemperatureRecord(10, TemperatureUnit.CELSIUS);

        var sum = roomTemperature.plus(add);
        Assertions.assertEquals(30.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, sum.unit());
    }

    @Test
    void addKelvinToCelsius() {
        var roomTemperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        var add = new TemperatureRecord(10, TemperatureUnit.KELVIN);

        var sum = roomTemperature.plus(add);
        Assertions.assertEquals(30.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, sum.unit());
    }

    @Test
    void addTwoFahrenheit() {
        var roomTemperature = new TemperatureRecord(70, TemperatureUnit.FAHRENHEIT);
        var add = new TemperatureRecord(30, TemperatureUnit.FAHRENHEIT);

        var sum = roomTemperature.plus(add);
        Assertions.assertEquals(100.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.FAHRENHEIT, sum.unit());
    }

    @Test
    void addRankineToFahrenheit() {
        var roomTemperature = new TemperatureRecord(70, TemperatureUnit.FAHRENHEIT);
        var add = new TemperatureRecord(30, TemperatureUnit.RANKINE);

        var sum = roomTemperature.plus(add);
        Assertions.assertEquals(100.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.FAHRENHEIT, sum.unit());
    }

    @Test
    void addCelsiusToFahrenheit() {
        var roomTemperature = new TemperatureRecord(70, TemperatureUnit.FAHRENHEIT);
        var add = new TemperatureRecord(10, TemperatureUnit.CELSIUS);

        var sum = roomTemperature.plus(add);
        Assertions.assertEquals(88.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.FAHRENHEIT, sum.unit());
    }

    @Test
    void addFahrenheitToCelsius() {
        var roomTemperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        var add = new TemperatureRecord(10, TemperatureUnit.FAHRENHEIT);

        var sum = roomTemperature.plus(add);
        Assertions.assertEquals(25.555, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, sum.unit());
    }
}