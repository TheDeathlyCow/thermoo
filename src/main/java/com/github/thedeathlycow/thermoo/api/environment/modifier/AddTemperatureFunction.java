package com.github.thedeathlycow.thermoo.api.environment.modifier;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;

public class AddTemperatureFunction implements CombinationFunction<TemperatureRecord> {
    @Override
    public TemperatureRecord combine(TemperatureRecord runningValue, TemperatureRecord shiftValue) {
        double baseKelvin = runningValue.valueInUnit(TemperatureUnit.KELVIN);
        double addKelvin = shiftValue.valueInUnit(TemperatureUnit.KELVIN);

        double sum = Math.max(baseKelvin + addKelvin, 0.0); // do not allow results below 0K
        return new TemperatureRecord(sum, TemperatureUnit.KELVIN);
    }
}