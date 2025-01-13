package com.github.thedeathlycow.thermoo.impl.temperature.effect;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.stream.Collectors;

public class TemperatureEffectManager {

    public static final TemperatureEffectManager INSTANCE = new TemperatureEffectManager();

    private final Map<RegistryKey<EntityType<?>>, Set<ConfiguredTemperatureEffect<?>>> entityTypeCache = new IdentityHashMap<>();

    private final Map<Identifier, ConfiguredTemperatureEffect<?>> registry = new HashMap<>();

    public Collection<ConfiguredTemperatureEffect<?>> getEffectsForEntity(LivingEntity entity) {
        EntityType<?> type = entity.getType();

        RegistryEntry.Reference<EntityType<?>> entry = type.getRegistryEntry();
        RegistryKey<EntityType<?>> key = entry.registryKey();

        return this.entityTypeCache.computeIfAbsent(
                key,
                ignored -> {
                    if (Thermoo.LOGGER.isDebugEnabled()) {
                        Thermoo.LOGGER.debug("Computing temperature effects for {}", key);
                    }
                    return this.registry.values()
                            .stream()
                            .filter(configuredEffect -> {
                                var allowedTypes = configuredEffect.entityTypes();
                                return allowedTypes.size() == 0 || type.isIn(allowedTypes);
                            })
                            .collect(Collectors.toSet());
                }
        );
    }

    public ConfiguredTemperatureEffect<?> getEffect(Identifier id) {
        return this.registry.getOrDefault(id, null);
    }

    public Collection<ConfiguredTemperatureEffect<?>> getAllEffects() {
        return this.registry.values();
    }

    void updateRegistry(Map<Identifier, ConfiguredTemperatureEffect<?>> effectsRegistry) {
        this.clearCache();

        this.registry.clear();
        this.registry.putAll(effectsRegistry);

        int numEffects = this.registry.size();
        Thermoo.LOGGER.info("Loaded {} temperature effect{}", numEffects, numEffects == 1 ? "" : "s");
    }

    private void clearCache() {
        this.entityTypeCache.clear();
        Thermoo.LOGGER.info("Clearing Entity Type to Temperature Effect cache");
    }

    private TemperatureEffectManager() {

    }
}