package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record TemperatureChangeImpl(
        Holder<TemperatureSource> source,
        @Nullable Entity cause,
        @Nullable Entity directCause,
        @Nullable Vec3 position
) implements TemperatureChange {
}