package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.core.v1.HeatingMode;
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware;

/**
 * A heating mode to apply the environment heat resistances. This is currently only used internally by {@link com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents}
 */
public final class EnvironmentHeatingMode implements HeatingMode {
    public static final EnvironmentHeatingMode INSTANCE = new EnvironmentHeatingMode();

    @Override
    public int applyResistance(TemperatureAware target, int temperatureChange) {
        double resistance = temperatureChange < 0
                ? target.thermoo$getEnvironmentColdResistance()
                : target.thermoo$getEnvironmentHeatResistance();

        if (resistance > 0) {
            return target.thermoo$getRandom().nextDouble() < resistance ? 0 : temperatureChange;
        } else if (resistance < 0) {
            return target.thermoo$getRandom().nextDouble() < -resistance ? 2 * temperatureChange : temperatureChange;
        } else {
            return temperatureChange;
        }
    }

    private EnvironmentHeatingMode() {

    }
}