package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface BuiltinTemperatureSources {
    TemperatureChange absolute();

    TemperatureChange active();

    TemperatureChange passive();

    TemperatureChange environment();
}