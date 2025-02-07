package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import net.minecraft.component.ComponentMap;
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
     * Looks up the current environment parameters for a world position
     *
     * @param world The world/level to lookup
     * @param pos   The position to lookup at
     * @return Returns an environment component map whose keys are defined by {@link EnvironmentComponentTypes}
     */
    ComponentMap findEnvironmentComponents(World world, BlockPos pos);

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