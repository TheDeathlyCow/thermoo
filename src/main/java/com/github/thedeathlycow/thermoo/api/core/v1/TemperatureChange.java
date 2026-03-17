package com.github.thedeathlycow.thermoo.api.core.v1;

import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.impl.core.TemperatureChangeImpl;
import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * The context of a specific temperature change.
 */
@ApiStatus.NonExtendable
public interface TemperatureChange {
    /**
     * The type of temperature change this is.
     */
    Holder<TemperatureSource> source();

    /**
     * The entity responsible for the change.
     *
     * @return Returns a nullable entity.
     */
    @Nullable
    Entity cause();

    /**
     * The entity directly responsible for the change. For example, with a freezing arrow shot by a Chillager, the
     * direct cause would be the arrow entity, and the {@link #cause()} would be the Chillager. If this is a
     * {@link #isDirect() direct} change, then this will be the same as the {@link #cause()}.
     *
     * @return Returns a nullable entity. This entity is {@code null} if and only if {@link #cause()} is null.
     */
    @Nullable
    Entity directCause();

    /**
     * The position of the temperature change source. May the same as the position of {@link #directCause()}, but may also
     * be something else if the direct cause is {@code null}.
     *
     * @return Returns a nullable vector 3.
     */
    @Nullable
    Vec3 position();

    /**
     * Applies the reduction of the temperature source.
     *
     * @param target            The target of the temperature change.
     * @param temperatureChange The original temperature change value.
     * @return Returns the adjusted temperature change.
     */
    int applyReduction(LivingEntity target, int temperatureChange);

    /**
     * Creates a simple temperature change context with only a type and no cause or position.
     *
     * @param source The type of the change, may not be {@code null}.
     */
    static TemperatureChange create(Holder<TemperatureSource> source) {
        Preconditions.checkNotNull(source);

        return new TemperatureChangeImpl(source, null, null, null);
    }

    /**
     * Creates a temperature change context with a type and a position, but no cause.
     *
     * @param source   The type of the change, may not be {@code null}.
     * @param position The position of the temperature change source, may not be {@code null}.
     */
    static TemperatureChange create(Holder<TemperatureSource> source, Vec3 position) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(position);

        return new TemperatureChangeImpl(source, null, null, position);
    }

    /**
     * Creates a temperature change context with a type and a directCause entity. If the {@code directCause} is a
     * {@link TraceableEntity} and has a non-null owner, then the {@link #cause()} will refer to the owner. Otherwise,
     * the {@link #directCause()} and {@link #cause()} will refer to the same entity.
     * In either case, the {@link #position()} will be the directCause's position.
     *
     * @param source      The type of the change, may not be {@code null}.
     * @param directCause The entity directly responsible for the change, may not be {@code null}.
     */
    static TemperatureChange create(Holder<TemperatureSource> source, Entity directCause) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(directCause);

        Entity cause = directCause;

        if (directCause instanceof TraceableEntity traceable) {
            Entity owner = traceable.getOwner();

            if (owner != null) {
                cause = owner;
            }
        }

        return new TemperatureChangeImpl(source, cause, directCause, directCause.position());
    }

    /**
     * Creates a temperature change context with a type, a cause entity, and a direct cause entity. The
     * {@link #position()} will be the direct cause's position.
     *
     * @param source      The type of the change, may not be {@code null}.
     * @param cause       The entity responsible for the change, may not be {@code null}.
     * @param directCause The entity directly responsible for the change, may not be {@code null}.
     */
    static TemperatureChange create(Holder<TemperatureSource> source, Entity cause, Entity directCause) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(cause);
        Preconditions.checkNotNull(directCause);

        return new TemperatureChangeImpl(source, cause, directCause, directCause.position());
    }

    /**
     * @return Returns {@code true} if the {@link #cause()} and {@link #directCause()} refer to the same entity.
     */
    default boolean isDirect() {
        return this.cause() == this.directCause();
    }

    /**
     * @return Returns {@code true} if the source of this change matches the {@code tag}.
     */
    default boolean is(final TagKey<TemperatureSource> tag) {
        return this.source().is(tag);
    }

    /**
     * @return Returns {@code true} if the source of this change matches the {@code typeKey}.
     */
    default boolean is(final ResourceKey<TemperatureSource> typeKey) {
        return this.source().is(typeKey);
    }
}