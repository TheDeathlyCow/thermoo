package com.github.thedeathlycow.thermoo.impl.neoforge.platform;

import com.github.thedeathlycow.thermoo.impl.ecs.SyncedIntEntityComponent;
import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSettingsComponent;
import com.github.thedeathlycow.thermoo.impl.neoforge.registry.ThermooAttachments;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooComponents;
import net.minecraft.world.entity.LivingEntity;

public class ThermooComponentsImpl implements ThermooComponents {
    @Override
    public SyncedIntEntityComponent getTemperatureComponent(LivingEntity provider) {
        return provider.getData(ThermooAttachments.TEMPERATURE);
    }

    @Override
    public SyncedIntEntityComponent getWetnessComponent(LivingEntity provider) {
        return provider.getData(ThermooAttachments.WETNESS);
    }

    @Override
    public TemperatureStatusSettingsComponent getTemperatureStatusSettings(LivingEntity provider) {
        return provider.getData(ThermooAttachments.TEMPERATURE_STATUS_SETTINGS);
    }

    @Override
    public void doSyncTemperatureComponent(LivingEntity provider) {
        provider.syncData(ThermooAttachments.TEMPERATURE);
    }

    @Override
    public void doSyncWetnessComponent(LivingEntity provider) {
        provider.syncData(ThermooAttachments.WETNESS);
    }
}