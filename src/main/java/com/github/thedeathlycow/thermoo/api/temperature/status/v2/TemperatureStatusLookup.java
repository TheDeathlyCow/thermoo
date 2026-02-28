package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureEffectsComponent;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;

public final class TemperatureStatusLookup {
    public static boolean isEnabled(Entity entity, ResourceKey<TemperatureStatus> key) {
        var component = TemperatureEffectsComponent.getNullable(entity);
        return component != null && component.isEffectEnabled(key);
    }

    public static boolean setEnabled(Entity entity, ResourceKey<TemperatureStatus> key, boolean value) {
        var component = TemperatureEffectsComponent.getNullable(entity);
        return component != null && component.setEffectEnabled(key, value);
    }

    public static boolean isEnabled(Entity entity, Holder.Reference<TemperatureStatus> status) {
        return isEnabled(entity, status);
    }

    public static boolean setEnabled(Entity entity, Holder.Reference<TemperatureStatus> status, boolean value) {
        return setEnabled(entity, status.key(), value);
    }

    private TemperatureStatusLookup() {

    }
}