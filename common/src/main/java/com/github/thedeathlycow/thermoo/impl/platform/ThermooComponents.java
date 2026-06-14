package com.github.thedeathlycow.thermoo.impl.platform;

import com.github.thedeathlycow.thermoo.impl.ecs.SyncedIntEntityComponent;
import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSettingsComponent;
import net.minecraft.world.entity.LivingEntity;

/// Platform independent abstraction of cardinal components/data attachments.
///
/// This is not a stable API!
public interface ThermooComponents {
    SyncedIntEntityComponent getTemperatureComponent(LivingEntity provider);

    SyncedIntEntityComponent getWetnessComponent(LivingEntity provider);

    TemperatureStatusSettingsComponent getTemperatureStatusSettings(LivingEntity provider);

    void doSyncTemperatureComponent(LivingEntity provider);

    void doSyncWetnessComponent(LivingEntity provider);
}