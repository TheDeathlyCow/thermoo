package com.github.thedeathlycow.thermoo.api;

public interface TemperatureAware {

    default int getTemperature() {
        return 0;
    }

    default void setTemperature(int temperature) {

    }

    default int getMinTemperature() {
        return 0;
    }

    default int getMaxTemperature() {
        return 0;
    }

    default void addTemperature(int temperatureDelta, TemperatureMode mode) {
    }

    default void addTemperature(int temperatureDelta) {
        this.addTemperature(temperatureDelta, TemperatureMode.ABSOLUTE);
    }


}
