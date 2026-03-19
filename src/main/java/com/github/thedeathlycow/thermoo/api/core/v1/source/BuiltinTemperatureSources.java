package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;

/**
 * Stores shared temperature change contexts for the {@linkplain TemperatureSources built in temperature sources}. All
 * sources stored by this class have no cause, direct cause, or position. Also provides convenience methods for creating
 * unshared instances from keys without needing to explicitly lookup sources.
 */
@ApiStatus.NonExtendable
public interface BuiltinTemperatureSources {
    /**
     * @return Returns a shared temperature change context with a source of {@link TemperatureSources#ABSOLUTE}
     */
    TemperatureChange absolute();

    /**
     * @return Returns a shared temperature change context with a source of {@link TemperatureSources#ACTIVE}
     */
    TemperatureChange active();

    /**
     * @return Returns a shared temperature change context with a source of {@link TemperatureSources#PASSIVE}
     */
    TemperatureChange passive();

    /**
     * @return Returns a shared temperature change context with a source of {@link TemperatureSources#ENVIRONMENT}
     */
    TemperatureChange environment();

    /**
     * Creates a simple temperature change context with only a type and no cause or position.
     *
     * @param sourceKey The key of the change source, may not be {@code null}.
     */
    TemperatureChange create(ResourceKey<TemperatureSource> sourceKey);

    /**
     * Creates a temperature change context with a type and a position, but no cause.
     *
     * @param sourceKey The key of the change source, may not be {@code null}.
     * @param position  The position of the temperature change source, may not be {@code null}.
     */
    TemperatureChange create(ResourceKey<TemperatureSource> sourceKey, Vec3 position);

    /**
     * Creates a temperature change context with a type and a directCause entity. If the {@code directCause} is a
     * {@link TraceableEntity} and has a non-null owner, then the {@link TemperatureChange#cause()} will refer to the owner. Otherwise,
     * the {@link TemperatureChange#directCause()} and {@link TemperatureChange#cause()} will refer to the same entity.
     * In either case, the {@link TemperatureChange#position()} will be the directCause's position.
     *
     * @param sourceKey   The key of the change source, may not be {@code null}.
     * @param directCause The entity directly responsible for the change, may not be {@code null}.
     */
    TemperatureChange create(ResourceKey<TemperatureSource> sourceKey, Entity directCause);

    /**
     * Creates a temperature change context with a type, a cause entity, and a direct cause entity. The
     * {@link TemperatureChange#position()} will be the direct cause's position.
     *
     * @param sourceKey   The key of the change source, may not be {@code null}.
     * @param cause       The entity responsible for the change, may not be {@code null}.
     * @param directCause The entity directly responsible for the change, may not be {@code null}.
     */
    TemperatureChange create(ResourceKey<TemperatureSource> sourceKey, Entity cause, Entity directCause);
}