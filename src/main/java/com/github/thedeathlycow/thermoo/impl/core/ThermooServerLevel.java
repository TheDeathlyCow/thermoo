package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;

import java.util.List;

public interface ThermooServerLevel {
    List<TemperatureChange> thermoo$tickingTemperatureSources();
}