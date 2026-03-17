package com.github.thedeathlycow.thermoo.impl.environment.attribute;

import com.github.thedeathlycow.thermoo.api.util.v1.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.v1.TemperatureUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemperatureModifierTest {
    @Test
    void sub10Kfrom30K() {
        var a = new TemperatureRecord(30, TemperatureUnit.KELVIN);
        var b = new TemperatureRecord(10, TemperatureUnit.KELVIN);

        var difference = TemperatureModifier.SUBTRACT.apply(a, b);

        assertEquals(new TemperatureRecord(20, TemperatureUnit.KELVIN), difference);
    }

    @Test
    void sub30Kfrom10K() {
        var a = new TemperatureRecord(30, TemperatureUnit.KELVIN);
        var b = new TemperatureRecord(10, TemperatureUnit.KELVIN);

        var difference = TemperatureModifier.SUBTRACT.apply(b, a);

        assertEquals(new TemperatureRecord(-20, TemperatureUnit.KELVIN), difference);
    }
}