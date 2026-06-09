package com.github.thedeathlycow.thermoo.impl.component;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureEffectsComponent;
import net.minecraft.world.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public final class ThermooComponents implements EntityComponentInitializer {

    public static final ComponentKey<EnvironmentComponent> TEMPERATURE = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature"),
            EnvironmentComponent.class
    );

    public static final ComponentKey<EnvironmentComponent> WETNESS = ComponentRegistry.getOrCreate(
            Thermoo.id("wetness"),
            EnvironmentComponent.class
    );

    public static final ComponentKey<TemperatureEffectsComponent> TEMPERATURE_EFFECTS = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature_effects"),
            TemperatureEffectsComponent.class
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
        registry.registerFor(
                LivingEntity.class,
                TEMPERATURE_EFFECTS,
                TemperatureEffectsComponent::new
        );
    }
}
