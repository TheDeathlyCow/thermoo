package com.github.thedeathlycow.thermoo.impl;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;

public final class ThermooComponents implements EntityComponentInitializer {

    public static final ComponentKey<IntComponent> TEMPERATURE = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature"),
            IntComponent.class
    );

    public static final ComponentKey<IntComponent> WETNESS = ComponentRegistry.getOrCreate(
            Thermoo.id("wetness"),
            IntComponent.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(
                LivingEntity.class,
                TEMPERATURE,
                TemperatureComponent::new
        );
        registry.registerFor(
                LivingEntity.class,
                WETNESS,
                WetnessComponent::new
        );
    }
}
