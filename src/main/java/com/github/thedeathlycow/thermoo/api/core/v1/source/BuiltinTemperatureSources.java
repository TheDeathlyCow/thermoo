package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.resources.ResourceKey;

public final class BuiltinTemperatureSources {
    public static final ResourceKey<TemperatureSource> ABSOLUTE = key("absolute");
    public static final ResourceKey<TemperatureSource> ACTIVE = key("active");
    public static final ResourceKey<TemperatureSource> PASSIVE = key("passive");

    private static ResourceKey<TemperatureSource> key(String id) {
        return ResourceKey.create(ThermooRegistryKeys.TEMPERATURE_SOURCE, Thermoo.id(id));
    }

    private BuiltinTemperatureSources() {

    }
}
