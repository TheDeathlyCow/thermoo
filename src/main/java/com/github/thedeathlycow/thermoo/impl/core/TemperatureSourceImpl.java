package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureReduction;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Range;

public record TemperatureSourceImpl(
        Component description,
        TemperatureReduction reduction,
        @Range(from = 0, to = Integer.MAX_VALUE) int tickInterval
) implements TemperatureSource {
    @Override
    public int applyReduction(TemperatureAware target, int temperatureChange) {
        return this.reduction.applyReduction(target, temperatureChange);
    }
}