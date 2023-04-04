package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingMode;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;

public class InitialTemperatureChangeResult extends InitialEnvironmentChangeResult<TemperatureAware> {

    private HeatingMode mode;

    public InitialTemperatureChangeResult(int initialTemperatureChange, HeatingMode mode) {
        super(initialTemperatureChange);
        this.mode = mode;
    }

    public HeatingMode getMode() {
        return mode;
    }

    public void setMode(HeatingMode mode) {
        this.mode = mode;
    }

    @Override
    public void applyChange(TemperatureAware affectee, int amount) {
        boolean canApply = (affectee.thermoo$canFreeze() && amount < 0)
                || (affectee.thermoo$canOverheat() && amount > 0);

        if (canApply) {
            affectee.thermoo$addTemperature(amount, this.mode);
        }
    }
}
