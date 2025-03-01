package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import net.minecraft.component.ComponentMap;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public record EnvironmentTickContextImpl<T extends TemperatureAware & Soakable>(
        T affected,
        ServerWorld world,
        BlockPos pos,
        ComponentMap components
) implements EnvironmentTickContext<T> {
}