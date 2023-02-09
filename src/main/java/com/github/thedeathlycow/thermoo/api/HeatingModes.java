package com.github.thedeathlycow.thermoo.api;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

/**
 * The three primary modes for adding and removing heat from a {@link TemperatureAware}.
 */
public enum HeatingModes implements HeatingMode, StringIdentifiable {
    /**
     * Applies temperature deltas absolutely - ignoring all resistance in all conditions.
     * Used as the default mode in commands or other debug environments.
     */
    ABSOLUTE("absolute") {
        @Override
        public int applyResistance(TemperatureAware target, int temperatureDelta) {
            return temperatureDelta;
        }
    },
    /**
     * Always applies the relevant resistance for the delta - cold resistance when the delta is negative (decreasing temperature)
     * and heat resistance when the delta is positive (increasing temperature).
     *
     * Used for non-environmental effects, like a Frostologer freezing their victim or an Enchantment that drains heat.
     */
    ACTIVE("active") {
        @Override
        public int applyResistance(TemperatureAware target, int temperatureDelta) {
            int currentTemperature = target.thermoo$getTemperature();
            boolean isDeltaFreezing = temperatureDelta < 0;

            double resistance = isDeltaFreezing ? target.thermoo$getColdResistance() : target.thermoo$getHeatResistance();
            return HeatingModes.applyResistanceToDelta(resistance, temperatureDelta);
        }
    },
    /**
     * Only applies thermal resistance when the target is currently in the relevant temperature range. For example, cold
     * resistance is only applied to targets that are cold; and heat resistance only to targets that are warm.
     *
     * Used for passive environmental effects, such as the temperature change of a biome or of a torch.
     */
    PASSIVE("passive") {
        @Override
        public int applyResistance(TemperatureAware target, int temperatureDelta) {
            int currentTemperature = target.thermoo$getTemperature();
            boolean isDeltaFreezing = temperatureDelta < 0;
            double resistance = 0.0;

            if (currentTemperature < 0 && isDeltaFreezing) {
                resistance = target.thermoo$getColdResistance();
            } else if (currentTemperature > 0 && !isDeltaFreezing) {
                resistance = target.thermoo$getHeatResistance();
            }

            return HeatingModes.applyResistanceToDelta(resistance, temperatureDelta);
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
     * Applies a cold/heat resistance value to a temperature delta.
     * Resistance values are on a scale of 0 - 10, where 0 = 0% and 10 = 100%.
     *
     * @param resistance A raw resistance value, on a scale of 0-10.
     * @param temperatureDelta The temperature delta
     * @return Returns
     */
    public static int applyResistanceToDelta(double resistance, int temperatureDelta) {

        double resistanceAsPercent = ((resistance * 10.0) / 100.0);

        return MathHelper.ceil((1 - resistanceAsPercent) * temperatureDelta);
    }
}
