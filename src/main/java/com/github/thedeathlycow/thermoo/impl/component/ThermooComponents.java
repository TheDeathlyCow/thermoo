package com.github.thedeathlycow.thermoo.impl.component;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;

public final class ThermooComponents implements EntityComponentInitializer {

    public static final ComponentKey<EnvironmentComponent> TEMPERATURE = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature"),
            EnvironmentComponent.class
    );

    public static final ComponentKey<EnvironmentComponent> WETNESS = ComponentRegistry.getOrCreate(
            Thermoo.id("wetness"),
            EnvironmentComponent.class
    );


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(
                LivingEntity.class,
                TEMPERATURE,
                EnvironmentComponent::new
        );
        registry.registerFor(
                LivingEntity.class,
                WETNESS,
                EnvironmentComponent::new
        );
    }
}
