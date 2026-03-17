package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import com.github.thedeathlycow.thermoo.api.core.v1.source.BuiltinTemperatureSources;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSources;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

public record BuiltinTemperatureSourcesImpl(
        TemperatureChange absolute,
        TemperatureChange active,
        TemperatureChange passive,
        TemperatureChange environment
) implements BuiltinTemperatureSources {
    public BuiltinTemperatureSourcesImpl(RegistryAccess access) {
        this(
                change(access, TemperatureSources.ABSOLUTE),
                change(access, TemperatureSources.ACTIVE),
                change(access, TemperatureSources.PASSIVE),
                change(access, TemperatureSources.ENVIRONMENT)
        );
    }

    private static TemperatureChange change(RegistryAccess access, ResourceKey<TemperatureSource> key) {
        return TemperatureChange.create(access.getOrThrow(key));
    }
}
