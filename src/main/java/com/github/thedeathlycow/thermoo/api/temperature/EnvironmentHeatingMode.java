package com.github.thedeathlycow.thermoo.api.temperature;

public final class EnvironmentHeatingMode implements HeatingMode {
    public static final EnvironmentHeatingMode INSTANCE = new EnvironmentHeatingMode();

    @Override
    public int applyResistance(TemperatureAware target, int temperatureChange) {
        double resistance = temperatureChange < 0
                ? target.thermoo$getEnvironmentColdResistance()
                : target.thermoo$getEnvironmentHeatResistance();

        return target.thermoo$getRandom().nextDouble() < resistance
                ? 0
                : temperatureChange;
    }

    private EnvironmentHeatingMode() {

    }
}