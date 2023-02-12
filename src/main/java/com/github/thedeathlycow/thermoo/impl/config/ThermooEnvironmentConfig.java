package com.github.thedeathlycow.thermoo.impl.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "thermoo.environment")
public class ThermooEnvironmentConfig implements ConfigData {

    boolean doDryBiomeNightFreezing = true;

    float passiveTemperatureChangeMinScale = -1.0f;
    float passiveTemperatureChangeMaxScale = 1.0f;

    double biomeTemperatureMultiplier = 4.0;

    double passiveFreezingMaxTemp = 0.25;

    double nightTimeTemperatureDecrease = 0.25;

    int rainWetnessIncrease = 1;
    int touchingWaterWetnessIncrease = 5;
    int dryRate = 1;
    int onFireDryDate = 50;
    float soakPercentFromWaterPotion = 0.5f;

    int onFireWarmRate = 50;
    int conduitPowerWarmthPerTick = 12;

    int warmthPerLightLevel = 2;
    int minLightForWarmth = 5;

    int ultrawarmThawRate = 15;

    float dryBiomeNightTemperature = 0.0f;

    public boolean doDryBiomeNightFreezing() {
        return doDryBiomeNightFreezing;
    }

    public float getPassiveTemperatureChangeMinScale() {
        return passiveTemperatureChangeMinScale;
    }

    public float getPassiveTemperatureChangeMaxScale() {
        return passiveTemperatureChangeMaxScale;
    }

    public double getBiomeTemperatureMultiplier() {
        return biomeTemperatureMultiplier;
    }

    public double getPassiveFreezingMaxTemp() {
        return passiveFreezingMaxTemp;
    }

    public double getNightTimeTemperatureDecrease() {
        return nightTimeTemperatureDecrease;
    }

    public int getRainWetnessIncrease() {
        return rainWetnessIncrease;
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

    public float getSoakPercentFromWaterPotion() {
        return soakPercentFromWaterPotion;
    }

    public int getOnFireWarmRate() {
        return onFireWarmRate;
    }

    public int getWarmthPerLightLevel() {
        return warmthPerLightLevel;
    }

    public int getMinLightForWarmth() {
        return minLightForWarmth;
    }

    public int getUltrawarmWarmRate() {
        return ultrawarmThawRate;
    }

    public float getDryBiomeNightTemperature() {
        return dryBiomeNightTemperature;
    }
}
