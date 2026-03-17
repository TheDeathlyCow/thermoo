package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.core.v1.Soakable;
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.core.v1.event.EnvironmentTickContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;

public record EnvironmentTickContextImpl<T extends TemperatureAware & Soakable>(
        T affected,
        ServerLevel level,
        BlockPos pos,
        DataComponentMap components
) implements EnvironmentTickContext<T> {
}