package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
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
    DataComponentMap findEnvironmentComponents(Level world, BlockPos pos);
}