package com.github.thedeathlycow.thermoo.api.core.v1;

import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;

/**
 * The three primary modes for adding and removing heat from a {@link TemperatureAware}.
 */
public enum HeatingModes implements HeatingMode, StringRepresentable {
    /**
     * Applies temperature changes absolutely - ignoring all resistance in all conditions.
     * Used as the default mode in commands or other debug environments.
     */
    ABSOLUTE("absolute") {
        @Override
        public int applyResistance(TemperatureAware target, int temperatureChange) {
            return temperatureChange;
        }
    },
    /**
     * Always applies the relevant resistance for the change - cold resistance when the change is negative
     * (decreasing temperature) and heat resistance when the change is positive (increasing temperature).
     * <p>
     * Used for non-environmental effects, like a Frostologer freezing their victim or an Enchantment that drains heat.
     */
    ACTIVE("active") {
        @Override
        public int applyResistance(TemperatureAware target, int temperatureChange) {
            boolean isChangeFreezing = temperatureChange < 0;

            double resistance = isChangeFreezing ? target.thermoo$getColdResistance() : target.thermoo$getHeatResistance();
            return HeatingModes.applyResistanceToDelta(resistance, temperatureChange);
        }
    },
    /**
     * Only applies thermal resistance when the target is currently in the relevant temperature range. For example, cold
     * resistance is only applied to targets that are cold; and heat resistance only to targets that are warm.
     * <p>
     * Used for passive effects, such as the temperature change of standing on ice or being near to a torch. This is not used for the
     * environment temperature in {@link com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents}.
     */
    PASSIVE("passive") {
        @Override
        public int applyResistance(TemperatureAware target, int temperatureChange) {
            int currentTemperature = target.thermoo$getTemperature();
            boolean isChangeFreezing = temperatureChange < 0;
            double resistance = 0.0;

            if (currentTemperature < 0 && isChangeFreezing) {
                resistance = target.thermoo$getColdResistance();
            } else if (currentTemperature > 0 && !isChangeFreezing) {
                resistance = target.thermoo$getHeatResistance();
            }

            return HeatingModes.applyResistanceToDelta(resistance, temperatureChange);
        }
    };

    private final String id;

    HeatingModes(String id) {
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this.id;
    }

    /**
     * Applies a cold/heat resistance value to a temperature change.
     * Resistance values are on a scale of 0 - 10, where 0 = 0% and 10 = 100%.
     *
     * @param resistance        A raw resistance value, on a scale of 0-10.
     * @param temperatureChange The temperature change
     * @return Returns
     */
    private static int applyResistanceToDelta(double resistance, int temperatureChange) {

        double resistanceAsPercent = ((resistance * 10.0) / 100.0);

        return Mth.ceil((1 - resistanceAsPercent) * temperatureChange);
    }
}
