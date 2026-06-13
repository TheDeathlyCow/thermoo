package com.github.thedeathlycow.thermoo.impl.ecs;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureEffectContextImpl;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusImpl;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface TemperatureStatusSettingsComponent {
    Map<ResourceKey<TemperatureStatus>, Settings> getSettings();

    LivingEntity getProvider();

    @Nullable
    Settings getSettings(Holder.Reference<TemperatureStatus> statusRef);

    default boolean setEffectEnabled(Holder.Reference<TemperatureStatus> statusRef, boolean enabled) {
        Settings settings = this.getSettings(statusRef);

        if (settings != null && settings.enabled() != enabled) {
            settings.setEnabled(enabled);

            // this is meant to ensure that the effect is cleaned up right away and not have to wait for the next
            // interval check, especially if that interval is long.
            if (!settings.enabled() && settings.applied()) {
                ((TemperatureStatusImpl) statusRef.value()).remove(this.getProvider(), TemperatureEffectContextImpl.INSTANCE);
                settings.setApplied(false);
            }

            return true;
        }
        return false;
    }

    default boolean isEffectEnabled(Holder.Reference<TemperatureStatus> statusRef) {
        Settings settings = this.getSettings(statusRef);

        if (settings != null) {
            return settings.enabled();
        }

        return false;
    }
}