package com.github.thedeathlycow.thermoo.api.core.v1;

import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.impl.core.TemperatureChangeImpl;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface TemperatureChange {
    Holder<TemperatureSource> source();

    @Nullable
    Entity cause();

    @Nullable
    Entity directCause();

    @Nullable
    Vec3 position();

    static TemperatureChange create(Holder<TemperatureSource> source) {
        return new TemperatureChangeImpl(source, null, null, null);
    }

    static TemperatureChange create(Holder<TemperatureSource> source, Vec3 position) {
        return new TemperatureChangeImpl(source, null, null, position);
    }

    static TemperatureChange create(Holder<TemperatureSource> source, Entity cause) {
        return new TemperatureChangeImpl(source, cause, cause, cause.position());
    }

    static TemperatureChange create(Holder<TemperatureSource> source, Entity cause, Entity directCause) {
        return new TemperatureChangeImpl(source, cause, directCause, directCause.position());
    }

    default boolean isDirect() {
        return this.cause() == this.directCause();
    }

    default boolean is(final TagKey<TemperatureSource> tag) {
        return this.source().is(tag);
    }

    default boolean is(final ResourceKey<TemperatureSource> typeKey) {
        return this.source().is(typeKey);
    }
}