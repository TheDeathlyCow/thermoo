package com.github.thedeathlycow.thermoo.impl.fabric.platform;

import com.github.thedeathlycow.thermoo.impl.ecs.SyncedIntEntityComponent;
import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSettingsComponent;
import com.github.thedeathlycow.thermoo.impl.fabric.registry.ThermooCardinalEntityComponents;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooComponents;
import net.minecraft.world.entity.LivingEntity;

public class ThermooComponentsImpl implements ThermooComponents {
    @Override
    public SyncedIntEntityComponent getTemperatureComponent(LivingEntity provider) {
        return ThermooCardinalEntityComponents.TEMPERATURE.get(provider);
    }

    @Override
    public SyncedIntEntityComponent getWetnessComponent(LivingEntity provider) {
        return ThermooCardinalEntityComponents.WETNESS.get(provider);
    }

    @Override
    public TemperatureStatusSettingsComponent getTemperatureStatusSettings(LivingEntity provider) {
        return ThermooCardinalEntityComponents.TEMPERATURE_STATUS_SETTINGS.get(provider);
    }

    @Override
    public void doSyncTemperatureComponent(LivingEntity provider) {
        ThermooCardinalEntityComponents.TEMPERATURE.sync(provider);
    }

    @Override
    public void doSyncWetnessComponent(LivingEntity provider) {
        ThermooCardinalEntityComponents.WETNESS.sync(provider);
    }
}