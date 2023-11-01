package com.github.thedeathlycow.thermoo.api.temperature;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

/**
 * The three primary modes for adding and removing heat from a {@link TemperatureAware}.
 */
public enum HeatingModes implements HeatingMode, StringIdentifiable {
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
     * Used for passive environmental effects, such as the temperature change of a biome or of a torch.
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
    public String asString() {
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

        return MathHelper.ceil((1 - resistanceAsPercent) * temperatureChange);
    }
}
