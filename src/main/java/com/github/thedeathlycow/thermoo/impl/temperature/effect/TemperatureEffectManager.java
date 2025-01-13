package com.github.thedeathlycow.thermoo.impl.temperature.effect;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.*;

public class TemperatureEffectManager {

    public static final TemperatureEffectManager INSTANCE = new TemperatureEffectManager();

    private final Map<Identifier, ConfiguredTemperatureEffect<?>> globalEffects = new HashMap<>();

    private final Map<RegistryKey<EntityType<?>>, Set<ConfiguredTemperatureEffect<?>>> entityTypeToEffect = new IdentityHashMap<>();

    public void populateRegistry(
            Map<Identifier, ConfiguredTemperatureEffect<?>> globalEffects,
            Map<RegistryKey<EntityType<?>>, Set<ConfiguredTemperatureEffect<?>>> entityTypeToEffect
    ) {
        this.globalEffects.clear();
        this.globalEffects.putAll(globalEffects);
        this.entityTypeToEffect.clear();
        this.entityTypeToEffect.putAll(entityTypeToEffect);
    }

    public Collection<ConfiguredTemperatureEffect<?>> getEffectsForEntity(LivingEntity entity) {
        EntityType<?> type = entity.getType();

        RegistryKey<EntityType<?>> key = type.getRegistryEntry().registryKey();
        Set<ConfiguredTemperatureEffect<?>> effects = entityTypeToEffect.get(key);

        if (effects == null) {
            return Collections.emptySet();
        }

        return effects;
    }

    public Collection<ConfiguredTemperatureEffect<?>> getGlobalEffects() {
        return globalEffects.values();
    }

    private TemperatureEffectManager() {

    }
}