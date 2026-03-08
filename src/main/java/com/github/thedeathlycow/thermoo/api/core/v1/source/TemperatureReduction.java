package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.util.function.Function;

public interface TemperatureReduction {
    /**
     * Codec for the temperature effect object.
     */
    Codec<TemperatureReduction> DIRECT_CODEC = ThermooRegistries.TEMPERATURE_REDUCTION_TYPE.byNameCodec()
            .dispatch(TemperatureReduction::codec, Function.identity());

    int applyReduction(TemperatureAware target, int temperatureChange);

    MapCodec<? extends TemperatureReduction> codec();
}