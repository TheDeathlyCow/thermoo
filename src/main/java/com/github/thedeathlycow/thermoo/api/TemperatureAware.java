package com.github.thedeathlycow.thermoo.api;

import org.apache.commons.lang3.NotImplementedException;

/**
 * TemperatureAware entities are things that are sensitive to temperature. In Thermoo, the Temperature of a Thermally-aware
 * object is an arbitrary unit that represents the total heat (or cold) they have collected. It is not based on standard
 * units of temperature like Celsius, Kelvin, or Fahrenheit. Positive values of temperature are treated as 'warm' and
 * negative values are treated as 'cold'.
 * <p>
 * This class is interface injected into {@link net.minecraft.entity.LivingEntity}. Therefore, ALL methods must have a
 * default implementation. Methods that should normally be abstract should throw a {@link NotImplementedException} instead
 * of being declared abstract.
 */
public interface TemperatureAware {


    /**
     * @return Returns the current temperature of the temperature aware object
     */
    default int thermoo$getTemperature() {
        throw new NotImplementedException();
    }

    /**
     * @param temperature Sets the current temperature of the temperature aware object to an exact value
     */
    default void thermoo$setTemperature(int temperature) {
        throw new NotImplementedException();
    }

    /**
     * @return Returns the minimum allowed temperature of the temperature aware object
     */
    default int thermoo$getMinTemperature() {
        throw new NotImplementedException();
    }

    /**
     * @return Returns the maximum allowed temperature of the temperature aware object
     */
    default int thermoo$getMaxTemperature() {
        throw new NotImplementedException();
    }

    /**
     * Supplies the cold resistance of the temperature aware object. Cold Resistance is a percentage on a scale from
     * 0 to 10, where 0 cold resistance corresponds to 0%, and 10 cold resistance corresponds to 100%.
     *
     * @return Returns the cold resistance of the temperature aware object
     */
    default double thermoo$getColdResistance() {
        throw new NotImplementedException();
    }

    /**
     * Supplies the heat resistance of the temperature aware object. Heat Resistance is a percentage on a scale from
     * 0 to 10, where 0 heat resistance corresponds to 0%, and 10 heat resistance corresponds to 100%.
     *
     * @return Returns the heat resistance of the temperature aware object
     */
    default double thermoo$getHeatResistance() {
        throw new NotImplementedException();
    }

    /**
     * @return Returns if the thermally aware object can be affected by cold
     */
    default boolean thermoo$canFreeze() {
        throw new NotImplementedException();
    }

    /**
     * @return Returns if the thermally aware object can be affected by heat
     */
    default boolean thermoo$canOverheat() {
        throw new NotImplementedException();
    }

    /**
     * Adds or removes some amount of temperature to the thermally aware object. Resistance can be applied by specifying
     * a {@link HeatingMode}. See {@link HeatingModes} for some common modes.
     *
     * @param temperatureDelta The amount of temperature to add/remove. Positive delta adds, negative delta removes.
     * @param mode             The mode of resistance to apply to the delta.
     */
    default void thermoo$addTemperature(int temperatureDelta, HeatingMode mode) {
        throw new NotImplementedException();
    }

    /**
     * Adds or removes some amount of temperature to the thermally aware object. Applies no resistance.
     *
     * @param temperatureDelta The amount of temperature to add/remove. Positive delta adds, negative delta removes.
     */
    default void thermoo$addTemperature(int temperatureDelta) {
        this.thermoo$addTemperature(temperatureDelta, HeatingModes.ABSOLUTE);
    }

    /**
     * @return Returns the current temperature as a -1 to +1 percentage scale of the minimum/maximum temperature.
     */
    default float thermoo$getTemperatureScale() {
        int temperature = this.thermoo$getTemperature();
        if (temperature == 0) {
            return 0.0f;
        } else if (temperature < 0) {
            return -((float) temperature) / this.thermoo$getMinTemperature();
        } else {
            return ((float) temperature) / this.thermoo$getMaxTemperature();
        }
    }

}
