package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Context objects for general temperature ticking events on temperature awares and soakables
 *
 * @param <T> The temperature aware type
 */
@ApiStatus.NonExtendable
public interface EnvironmentTickContext<T extends TemperatureAware & Soakable> {
    /**
     * The temperature aware/soakable being ticked
     */
    @NotNull
    T affected();

    /**
     * The server world of the affected temperature aware/soakable
     */
    @NotNull
    ServerWorld world();

    /**
     * The block position of the affected temperature aware/soakable. This should be preferred over using methods such as
     * {@link LivingEntity#getBlockPos()} since it can correct for being slightly sunk into blocks like mud or soul sand
     * by taking the block position that is slightly above their actual {@linkplain LivingEntity#getPos() position}.
     */
    @NotNull
    BlockPos pos();

    /**
     * The current environment components at the world and position.
     * <p>
     * No key is guaranteed to be mapped to a value, be sure to always check the result or use {@link ComponentMap#getOrDefault(ComponentType, Object)}.
     * <p>
     * Environment components are only looked up for players by default, for all other entity types this map is empty.
     *
     * @return Returns an {@link EnvironmentComponentTypes environment component map}
     */
    ComponentMap components();
}