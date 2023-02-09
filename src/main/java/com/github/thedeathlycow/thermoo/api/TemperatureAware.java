package com.github.thedeathlycow.thermoo.api;

import org.apache.commons.lang3.NotImplementedException;

public interface TemperatureAware {

    default int getTemperature() {
        throw new NotImplementedException();
    }

    default void setTemperature(int temperature) {
        throw new NotImplementedException();
    }

    default int getMinTemperature() {
        throw new NotImplementedException();
    }

    default int getMaxTemperature() {
        throw new NotImplementedException();
    }

    default double getColdResistance() {
        throw new NotImplementedException();
    }

    default double getHeatResistance() {
        throw new NotImplementedException();
    }

    default void addTemperature(int temperatureDelta, HeatingMode mode) {
        throw new NotImplementedException();
    }

    default void addTemperature(int temperatureDelta) {
        this.addTemperature(temperatureDelta, HeatingModes.ABSOLUTE);
    }

    default float getTemperatureScale() {
        int temperature = this.getTemperature();
        if (temperature < 0) {
            return -((float) temperature) / this.getMinTemperature();
        } else {
            return ((float) temperature) / this.getMaxTemperature();
        }
    }

}
