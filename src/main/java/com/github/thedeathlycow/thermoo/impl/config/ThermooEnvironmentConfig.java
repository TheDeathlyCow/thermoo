package com.github.thedeathlycow.thermoo.impl.config;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "thermoo.environment")
public class ThermooEnvironmentConfig implements ConfigData {

    @ConfigEntry.Gui.RequiresRestart
    String controller = Thermoo.MODID;
    boolean doDryBiomeNightFreezing = true;

    double biomeTemperatureMultiplier = 4.0;

    double passiveFreezingCutoffTemp = 0.25;

    double nightTimeTemperatureDecrease = 0.25;

    int rainWetnessIncrease = 1;
    int touchingWaterWetnessIncrease = 5;
    int dryRate = 1;
    int onFireDryDate = 50;

    int onFireWarmRate = 50;

    int warmthPerLightLevel = 2;
    int minLightForWarmth = 5;

    int ultrawarmWarmRate = 15;

    float dryBiomeNightTemperature = 0.0f;

    public String getController() {
        return controller;
    }

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
        return ultrawarmWarmRate;
    }

    public float getDryBiomeNightTemperature() {
        return dryBiomeNightTemperature;
    }
}
