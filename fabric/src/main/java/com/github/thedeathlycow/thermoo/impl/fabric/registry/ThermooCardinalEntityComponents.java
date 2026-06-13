package com.github.thedeathlycow.thermoo.impl.fabric.registry;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.fabric.cca.SyncedIntCardinalComponent;
import com.github.thedeathlycow.thermoo.impl.fabric.cca.TemperatureStatusSettingsComponentImpl;
import com.github.thedeathlycow.thermoo.impl.fabric.cca.TemperatureStatusSystemImpl;
import net.minecraft.world.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class ThermooCardinalEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<SyncedIntCardinalComponent> TEMPERATURE = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature"),
            SyncedIntCardinalComponent.class
    );

    public static final ComponentKey<SyncedIntCardinalComponent> WETNESS = ComponentRegistry.getOrCreate(
            Thermoo.id("wetness"),
            SyncedIntCardinalComponent.class
    );

    public static final ComponentKey<TemperatureStatusSettingsComponentImpl> TEMPERATURE_STATUS_SETTINGS = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature_effects"), // for backwards compatibility, the old id is still used
            TemperatureStatusSettingsComponentImpl.class
    );

    public static final ComponentKey<TemperatureStatusSystemImpl> TEMPERATURE_STATUS_SYSTEM = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature_status_system"), // for backwards compatibility, the old id is still used
            TemperatureStatusSystemImpl.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(
                LivingEntity.class,
                TEMPERATURE,
                SyncedIntCardinalComponent::new
        );
        registry.registerFor(
                LivingEntity.class,
                WETNESS,
                SyncedIntCardinalComponent::new
        );
        registry.registerFor(
                LivingEntity.class,
                TEMPERATURE_STATUS_SETTINGS,
                TemperatureStatusSettingsComponentImpl::new
        );
        registry.registerFor(
                LivingEntity.class,
                TEMPERATURE_STATUS_SYSTEM,
                TemperatureStatusSystemImpl::new
        );
    }
}