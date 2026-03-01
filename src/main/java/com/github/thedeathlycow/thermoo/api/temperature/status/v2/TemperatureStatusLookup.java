package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureEffectsComponent;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;

/**
 * Provides interfaces for looking up data about {@linkplain TemperatureStatus temperature statuses} on entities.
 */
public final class TemperatureStatusLookup {
    /**
     * Checks if the given temperature status key is enabled on the entity.
     *
     * @return Returns {@code true} if the status could ever be applied to the entity and the status is currently
     * enabled on the entity. Will return {@code false} if the status has never tried to apply since the last server
     * start.
     */
    public static boolean isEnabled(Entity entity, ResourceKey<TemperatureStatus> key) {
        var component = TemperatureEffectsComponent.getNullable(entity);
        return component != null && component.isEffectEnabled(key);
    }

    /**
     * Checks if the given temperature status reference is enabled on the entity. Will return {@code false} until the
     * entity attempts to have the effect applied to them.
     *
     * @return Returns {@code true} if the status could ever be applied to the entity and the status is currently
     * enabled on the entity. Will return {@code false} if the status has never tried to apply since the last server
     * start.
     */
    public static boolean isEnabled(Entity entity, Holder.Reference<TemperatureStatus> status) {
        return isEnabled(entity, status.key());
    }

    /**
     * Enables or disables the given temperature status for the given entity. This state is persisted.
     *
     * @return Returns {@code true} if the status could ever be applied to the entity and if this call actually changes
     * the enabled value.
     */
    public static boolean setEnabled(Entity entity, ResourceKey<TemperatureStatus> key, boolean value) {
        var component = TemperatureEffectsComponent.getNullable(entity);
        return component != null && component.setEffectEnabled(key, value);
    }

    /**
     * Enables or disables the given temperature status for the given entity. This is persisted but will not work until
     * the status is attempted to be applied internally at least once on this entity.
     *
     * @return Returns {@code true} if the status could ever be applied to the entity and if this call actually changes
     * the enabled value.
     */
    public static boolean setEnabled(Entity entity, Holder.Reference<TemperatureStatus> status, boolean value) {
        return setEnabled(entity, status.key(), value);
    }

    private TemperatureStatusLookup() {

    }
}