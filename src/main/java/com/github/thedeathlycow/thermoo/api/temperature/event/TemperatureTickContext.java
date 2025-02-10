package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

/**
 * Context objects for general temperature ticking events on temperature awares
 *
 * @param <T> The temperature aware type
 */
public interface TemperatureTickContext<T extends TemperatureAware> {
    /**
     * The temperature aware being ticked
     */
    @NotNull
    T affected();

    /**
     * The server world of the affected temperature aware
     */
    @NotNull
    ServerWorld world();

    /**
     * The block position of the affected temperature aware. This should be preferred over using methods such as
     * {@link LivingEntity#getBlockPos()} since it can correct for being slightly sunk into blocks like mud or soul sand
     * by taking the block position that is slightly above their actual {@linkplain LivingEntity#getPos() position}.
     */
    @NotNull
    BlockPos pos();
}