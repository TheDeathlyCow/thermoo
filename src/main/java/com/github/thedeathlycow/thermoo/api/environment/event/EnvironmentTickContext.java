package com.github.thedeathlycow.thermoo.api.environment.event;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import net.minecraft.component.ComponentMap;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Context objects for temperature aware ticking events
 *
 * @param <T> The temperature aware type
 */
@ApiStatus.NonExtendable
public interface EnvironmentTickContext<T extends TemperatureAware> {
    /**
     * @return Returns the affected temperature aware
     */
    @NotNull
    T affected();

    /**
     * @return Returns the server world of the affected temperature aware
     */
    @NotNull
    ServerWorld world();

    /**
     * @return Returns the block position of the temperature aware in the world
     */
    @NotNull
    BlockPos pos();

    /**
     * The current environment components at the world and position.
     * <p>
     * This map is only non-empty for {@link ServerPlayerEnvironmentTickEvents#GET_TEMPERATURE_CHANGE} and later events.
     *
     * @return Returns an {@link EnvironmentComponentTypes environment component map}
     */
    ComponentMap components();
}
