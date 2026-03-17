package com.github.thedeathlycow.thermoo.api.core.v1.event;

import com.github.thedeathlycow.thermoo.api.core.v1.Soakable;
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
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
     * The server level of the affected temperature aware/soakable
     */
    @NotNull
    ServerLevel level();

    /**
     * The block position of the affected temperature aware/soakable. This should be preferred over using methods such as
     * {@link LivingEntity#blockPosition()} since it can correct for being slightly sunk into blocks like mud or soul sand
     * by taking the block position that is slightly above their actual {@linkplain LivingEntity#position() position}.
     */
    @NotNull
    BlockPos pos();

    /**
     * The current environment components at the world and position.
     * <p>
     * No key is guaranteed to be mapped to a value, be sure to always check the result or use {@link DataComponentMap#getOrDefault(net.minecraft.core.component.DataComponentType, Object)}.
     * <p>
     * Environment components are only looked up for players by default, for all other entity types this map is empty.
     *
     * @return Returns an {@link EnvironmentComponentTypes environment component map}
     */
    DataComponentMap components();
}