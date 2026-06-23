package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectContext;

public final class TemperatureEffectContextImpl implements TemperatureEffectContext {
    public static final TemperatureEffectContextImpl INSTANCE = new TemperatureEffectContextImpl();

    private TemperatureEffectContextImpl() {

    }
}