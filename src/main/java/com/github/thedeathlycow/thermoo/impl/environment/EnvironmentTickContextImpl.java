package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;

public record EnvironmentTickContextImpl<T extends TemperatureAware & Soakable>(
        T affected,
        ServerLevel world,
        BlockPos pos,
        DataComponentMap components
) implements EnvironmentTickContext<T> {
}