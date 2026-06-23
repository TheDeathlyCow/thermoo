package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * Provides interfaces for looking up data about {@linkplain TemperatureStatus temperature statuses} on entities.
 */
public final class TemperatureStatusLookup {
    /**
     * Checks if a temperature status is enabled for the given entity.
     *
     * @return Returns {@code true} if the status is currently enabled AND the entity's type is supported by the status
     * selector; returns {@code false} otherwise.
     */
    public static boolean isEnabled(LivingEntity entity, Holder.Reference<TemperatureStatus> statusRef) {
        return ThermooServices.COMPONENTS.getTemperatureStatusSettings(entity).isEffectEnabled(statusRef);
    }

    /**
     * Checks if a temperature status is enabled for the given entity.
     *
     * @return Returns {@code true} if the status is currently enabled AND the entity's type is supported by the status
     * selector AND the {@code entity} is an instance of {@link LivingEntity}; returns {@code false} otherwise.
     */
    public static boolean isEnabled(Entity entity, Holder.Reference<TemperatureStatus> statusRef) {
        return entity instanceof LivingEntity livingEntity && isEnabled(livingEntity, statusRef);
    }

    /**
     * Sets the enabled state of a temperature status for an entity. This state is persisted.
     *
     * @return Returns {@code true} if the entity's type is supported by the status selector AND the enabled state was
     * successfully changed; returns {@code false} otherwise.
     */
    public static boolean setEnabled(LivingEntity entity, Holder.Reference<TemperatureStatus> statusRef, boolean value) {
        return ThermooServices.COMPONENTS.getTemperatureStatusSettings(entity).setEffectEnabled(statusRef, value);
    }

    /**
     * Sets the enabled state of a temperature status for an entity. This state is persisted.
     *
     * @return Returns {@code true} if the entity's type is supported by the status selector AND the {@code entity} is
     * an instance of {@link LivingEntity} AND the enabled state was successfully changed; returns {@code false} otherwise.
     */
    public static boolean setEnabled(Entity entity, Holder.Reference<TemperatureStatus> statusRef, boolean value) {
        return entity instanceof LivingEntity livingEntity && setEnabled(livingEntity, statusRef, value);
    }

    private TemperatureStatusLookup() {

    }
}