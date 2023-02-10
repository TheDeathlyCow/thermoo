package com.github.thedeathlycow.thermoo.api.temperature;

/**
 * The mode of temperature change for a {@link TemperatureAware}.
 * Applies resistances to temperature changes under various conditions.
 */
public interface HeatingMode {

    /**
     * Applies thermal resistance to a {@link TemperatureAware} target
     *
     * @param target            The thermally-aware target
     * @param temperatureChange The temperature change to apply resistance to
     * @return Returns the adjusted delta after applying resistance.
     */
    int applyResistance(TemperatureAware target, int temperatureChange);

}
