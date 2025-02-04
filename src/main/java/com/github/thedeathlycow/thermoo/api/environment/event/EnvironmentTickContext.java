package com.github.thedeathlycow.thermoo.api.environment.event;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface EnvironmentTickContext<T extends TemperatureAware> {
    T affected();

    ServerWorld world();

    BlockPos pos();

    TemperatureRecord temperature();

    double relativeHumidity();
}
