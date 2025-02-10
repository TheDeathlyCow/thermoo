package com.github.thedeathlycow.thermoo.testmod.config;

public class ThermooEnvironmentConfig {

    boolean doDryBiomeNightFreezing = true;

    double biomeTemperatureMultiplier = 4.0;

    double passiveFreezingCutoffTemp = 0.25;

    double nightTimeTemperatureDecrease = 0.25;
    int touchingWaterWetnessIncrease = 5;
    int dryRate = 1;
    int onFireDryDate = 50;
    int ultrawarmWarmRate = 15;
    float dryBiomeNightTemperature = 0.0f;

    public boolean doDryBiomeNightFreezing() {
        return doDryBiomeNightFreezing;
    }

    public double getBiomeTemperatureMultiplier() {
        return biomeTemperatureMultiplier;
    }

    public double getPassiveFreezingCutoffTemp() {
        return passiveFreezingCutoffTemp;
    }

    public double getNightTimeTemperatureDecrease() {
        return nightTimeTemperatureDecrease;
    }

    public int getTouchingWaterWetnessIncrease() {
        return touchingWaterWetnessIncrease;
    }

    public int getDryRate() {
        return dryRate;
    }

    public int getOnFireDryDate() {
        return onFireDryDate;
    }

    public int getUltrawarmWarmRate() {
        return ultrawarmWarmRate;
    }

    public float getDryBiomeNightTemperature() {
        return dryBiomeNightTemperature;
    }
}
