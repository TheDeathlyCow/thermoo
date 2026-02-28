package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectV2;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusDefinition;

import java.util.List;

public record TemperatureStatusImpl(
        TemperatureStatusDefinition definition,
        List<TemperatureEffectV2> effects
) implements TemperatureStatus {
}