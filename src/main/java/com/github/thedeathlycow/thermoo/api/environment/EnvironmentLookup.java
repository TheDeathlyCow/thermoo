package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;

/**
 * This interface provides facilities for looking up environment values from the {@link EnvironmentDefinition}s.
 * <p>
 * Should only be extended by Thermoo.
 */
@ApiStatus.NonExtendable
public interface EnvironmentLookup {
    /**
     * Gets the singleton instance of this interface
     */
    static EnvironmentLookup getInstance() {
        return EnvironmentLookupImpl.INSTANCE;
    }

    /**
     * Finds the current environmental temperature at a world position in a given unit. If no {@link EnvironmentDefinition}
     * exists for the biome at this position, or does not provide a temperature, will return the
     * {@linkplain #fallbackTemperature(TemperatureUnit) fallback temperature} (a comfortable 20C).
     *
     * @param world The world to query in
     * @param pos   The position to lookup
     * @param unit  The requested unit of the returned temperature value
     * @return Returns the current temperature value in the given unit for the world position.
     */
    double findTemperature(World world, BlockPos pos, TemperatureUnit unit);

    /**
     * Finds the current relative humidity at a world position. If no {@link EnvironmentDefinition} exists for the biome
     * at this position, or does not provide a relative humidity, will return the
     * {@linkplain #fallbackRelativeHumidity() fallback humidity} (a comfortable 50%).
     * <p>
     * Relative humidity is "the ratio of how much water vapour is in the air to how much water vapour the air could
     * potentially contain" <a href="https://en.m.wikipedia.org/wiki/Humidity#Relative_humidity">[1]</a> and is
     * expressed here on a 0-1 scale.
     *
     * @param world The world to query in
     * @param pos   The position to lookup
     * @return Returns the relative humidity as a 0-1 percentage.
     * @see <a href="https://en.m.wikipedia.org/wiki/Humidity">Humidity on Wikipedia</a>
     */
    double findRelativeHumidity(World world, BlockPos pos);

    /**
     * Gets the fallback temperature of 20C in a given unit
     *
     * @param unit the unit to get the fallback temperature for
     * @return returns a temperature value in the given unit
     */
    static double fallbackTemperature(TemperatureUnit unit) {
        return unit.fromCelsius(20.0);
    }

    /**
     * @return Returns the fallback relative humidity of 50%
     */
    static double fallbackRelativeHumidity() {
        return 0.5;
    }
}