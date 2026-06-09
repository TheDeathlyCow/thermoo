package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record TemperatureChangeImpl(
        Holder<TemperatureSource> source,
        @Nullable Entity cause,
        @Nullable Entity directCause,
        @Nullable Vec3 position
) implements TemperatureChange {
    @Override
    public int applyReduction(LivingEntity target, int temperatureChange) {
        return this.source.value().reduction()
                .map(reduction -> reduction.applyReduction(target, this, temperatureChange))
                .orElse(temperatureChange);
    }
}