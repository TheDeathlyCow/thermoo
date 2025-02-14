package com.github.thedeathlycow.thermoo.api.temperature;

public final class EnvironmentHeatingMode implements HeatingMode {
    public static final EnvironmentHeatingMode INSTANCE = new EnvironmentHeatingMode();

    @Override
    public int applyResistance(TemperatureAware target, int temperatureChange) {
        return 0;
    }

    private EnvironmentHeatingMode() {

    }
}