package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureReduction;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Range;

public record TemperatureSourceImpl(
        Component description,
        TemperatureReduction reduction,
        @Range(from = 0, to = Integer.MAX_VALUE) int tickInterval
) implements TemperatureSource {
    @Override
    public int applyReduction(LivingEntity target, int temperatureChange) {
        return this.reduction.applyReduction(target, temperatureChange);
    }
}