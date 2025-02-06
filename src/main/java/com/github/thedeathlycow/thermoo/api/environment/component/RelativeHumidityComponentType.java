package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.mojang.serialization.Codec;

import java.util.Collection;

public class RelativeHumidityComponentType implements MergedComponentType<Double> {
    public static final Codec<RelativeHumidityComponentType> CODEC = Codec.doubleRange(0.0, 1.0)
            .xmap(RelativeHumidityComponentType::new, RelativeHumidityComponentType::value);

    public static final double FALLBACK_HUMIDITY = 0.5;

    private final Double value;

    public RelativeHumidityComponentType(Double value) {
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }

    @Override
    public Double merge(Collection<Double> values) {
        return values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(FALLBACK_HUMIDITY);
    }
}