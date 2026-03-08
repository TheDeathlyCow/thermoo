package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

@ApiStatus.NonExtendable
public interface TemperatureSource {
    Component description();

    Holder<TemperatureReduction> reduction();

    @Range(from = 0, to = Integer.MAX_VALUE)
    int tickInterval();

    int applyReduction(TemperatureAware target, int temperatureChange);
}
