package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureReduction;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Range;

import java.util.Optional;

public record TemperatureSourceImpl(
        Component description,
        Optional<TemperatureReduction> reduction,
        @Range(from = 0, to = Integer.MAX_VALUE) int tickInterval
) implements TemperatureSource {
}