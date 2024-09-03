package com.github.thedeathlycow.thermoo.api.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TemperatureConverterTest {

    @Test
    void t20c_is_neutral() {
        int tempChange = TemperatureConverter.celsiusToTemperatureTick(20.0);
        Assertions.assertEquals(0, tempChange);
    }

    @Test
    void neutral_is_20c() {
        double celsius = TemperatureConverter.temperatureTickToCelsius(0);
        Assertions.assertEquals(20.0, celsius, 1e-2);
    }

    @Test
    void t68f_is_neutral() {
        int tempChange = TemperatureConverter.ambientTemperatureToTemperatureTick(
                68.0, TemperatureConverter.Settings.DEFAULT_FAHRENHEIT
        );
        Assertions.assertEquals(0, tempChange);
    }

    @Test
    void neutral_is_68f() {
        double celsius = TemperatureConverter.temperatureTickToAmbientTemperature(
                0, TemperatureConverter.Settings.DEFAULT_FAHRENHEIT
        );
        Assertions.assertEquals(68.0, celsius, 1e-2);
    }

}
