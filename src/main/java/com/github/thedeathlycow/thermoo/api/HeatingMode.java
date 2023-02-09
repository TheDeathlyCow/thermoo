package com.github.thedeathlycow.thermoo.api;

/**
 * The mode of temperature increase/decrease for a {@link TemperatureAware}.
 * Applies resistances to temperature deltas under various conditions.
 */
public interface HeatingMode {

    /**
     * Applies thermal resistance to a {@link TemperatureAware} target
     * @param target The thermally-aware target
     * @param temperatureDelta The temperature delta to apply resistance to
     * @return Returns the adjusted delta after applying resistance.
     */
    int applyResistance(TemperatureAware target, int temperatureDelta);

}
