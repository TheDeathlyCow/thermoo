package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import net.minecraft.core.Holder;

import java.util.List;

public interface ThermooServerLevel {
    List<Holder.Reference<TemperatureSource>> thermoo$tickingTemperatureSources();
}