package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingMode;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;

/**
 * Initial Environment change result implemented for {@link TemperatureAware}s
 */
public class InitialTemperatureChangeResult extends InitialEnvironmentChangeResult<TemperatureAware> {

    private HeatingMode mode;

    /**
     * Constructs a result with an initial amount and a mode
     *
     * @param temperatureAware         The temperature aware entity to apply the change to
     * @param initialTemperatureChange The initial temp change to apply
     * @param mode                     The mode to apply the temp change in
     */
    public InitialTemperatureChangeResult(TemperatureAware temperatureAware, int initialTemperatureChange, HeatingMode mode) {
        super(temperatureAware, initialTemperatureChange);
        this.mode = mode;
    }

    /**
     * @return Returns the mode of the temperature change
     */
    public HeatingMode getMode() {
        return mode;
    }

    /**
     * Sets the heating mode of the change
     *
     * @param mode The new mode of the change
     */
    public void setMode(HeatingMode mode) {
        this.mode = mode;
    }

    /**
     * Applies the temperature change in the mode of the result to the affectee
     *
     * @param affectee The affectee of the change
     * @param amount   The amount of change to apply
     */
    @Override
    protected void applyChange(TemperatureAware affectee, int amount) {
        affectee.thermoo$addTemperature(amount, this.mode);
    }
}
