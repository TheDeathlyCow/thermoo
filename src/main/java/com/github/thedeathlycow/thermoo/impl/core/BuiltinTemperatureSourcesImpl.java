package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.github.thedeathlycow.thermoo.api.core.v2.source.BuiltinTemperatureSources;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSources;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record BuiltinTemperatureSourcesImpl(
        TemperatureChange absolute,
        TemperatureChange active,
        TemperatureChange passive,
        TemperatureChange environment,
        RegistryAccess access
) implements BuiltinTemperatureSources {
    public BuiltinTemperatureSourcesImpl(RegistryAccess access) {
        this(
                create(access, TemperatureSources.ABSOLUTE),
                create(access, TemperatureSources.ACTIVE),
                create(access, TemperatureSources.PASSIVE),
                create(access, TemperatureSources.ENVIRONMENT),
                access
        );
    }

    @Override
    public TemperatureChange create(ResourceKey<TemperatureSource> sourceKey) {
        return TemperatureChange.create(lookup(this.access, sourceKey));
    }

    @Override
    public TemperatureChange create(ResourceKey<TemperatureSource> sourceKey, Vec3 position) {
        return TemperatureChange.create(lookup(this.access, sourceKey), position);
    }

    @Override
    public TemperatureChange create(ResourceKey<TemperatureSource> sourceKey, Entity directCause) {
        return TemperatureChange.create(lookup(this.access, sourceKey), directCause);
    }

    @Override
    public TemperatureChange create(ResourceKey<TemperatureSource> sourceKey, Entity cause, Entity directCause) {
        return TemperatureChange.create(lookup(this.access, sourceKey), cause, directCause);
    }

    private static Holder<TemperatureSource> lookup(RegistryAccess access, ResourceKey<TemperatureSource> key) {
        return access.getOrThrow(key);
    }

    private static TemperatureChange create(RegistryAccess access, ResourceKey<TemperatureSource> key) {
        return TemperatureChange.create(lookup(access, key));
    }
}
