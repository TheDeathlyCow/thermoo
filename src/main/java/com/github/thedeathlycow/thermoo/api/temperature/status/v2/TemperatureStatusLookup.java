package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureEffectsComponent;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;

public final class TemperatureStatusLookup {
    public static boolean isEnabled(LivingEntity entity, ResourceKey<TemperatureStatus> key) {
        return TemperatureEffectsComponent.get(entity).isEffectEnabled(key);
    }

    public static boolean setEnabled(LivingEntity entity, ResourceKey<TemperatureStatus> key, boolean value) {
        return TemperatureEffectsComponent.get(entity).setEffectEnabled(key, value);
    }

    public static boolean isEnabled(LivingEntity entity, Holder<TemperatureStatus> status) {
        ResourceKey<TemperatureStatus> key = status.unwrapKey().orElse(null);

        if (key == null) {
            return false;
        }

        return isEnabled(entity, key);
    }

    public static boolean setEnabled(LivingEntity entity, Holder<TemperatureStatus> status, boolean value) {
        ResourceKey<TemperatureStatus> key = status.unwrapKey().orElse(null);

        if (key == null) {
            return false;
        }

        return setEnabled(entity, key, value);
    }

    private TemperatureStatusLookup() {

    }
}