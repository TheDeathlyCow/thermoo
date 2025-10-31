package com.github.thedeathlycow.thermoo.impl.temperature.effect;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;
import java.util.stream.Collectors;

public class TemperatureEffectManager {

    public static final TemperatureEffectManager INSTANCE = new TemperatureEffectManager();

    private final Map<ResourceKey<EntityType<?>>, Set<EntityTypeCacheEntry>> entityTypeCache = new IdentityHashMap<>();

    private final Map<ResourceLocation, ConfiguredTemperatureEffect<?>> registry = new HashMap<>();

    public Set<EntityTypeCacheEntry> getEffectsEntriesForEntity(LivingEntity entity) {
        EntityType<?> type = entity.getType();

        Holder.Reference<EntityType<?>> entityTypeEntry = type.builtInRegistryHolder();
        ResourceKey<EntityType<?>> entityTypeKey = entityTypeEntry.key();

        Set<EntityTypeCacheEntry> effects = this.entityTypeCache.computeIfAbsent(
                entityTypeKey,
                ignored -> {
                    if (Thermoo.LOGGER.isDebugEnabled()) {
                        Thermoo.LOGGER.debug("Computing temperature effects for {}", entityTypeKey);
                    }
                    return this.registry.entrySet()
                            .stream()
                            .filter(entry -> {
                                var allowedTypes = entry.getValue().entityTypes();
                                return allowedTypes.size() == 0 || type.isIn(allowedTypes);
                            })
                            .map(EntityTypeCacheEntry::new)
                            .collect(Collectors.toUnmodifiableSet());
                }
        );

        if (Thermoo.LOGGER.isDebugEnabled()) {
            ResourceLocation[] effectIds = effects.stream()
                    .map(entry -> entry.location)
                    .toArray(ResourceLocation[]::new);
            Thermoo.LOGGER.debug("Available Temperature Effects for {}: {}", entityTypeKey, Arrays.toString(effectIds));
        }

        return effects;
    }

    public ConfiguredTemperatureEffect<?> getEffect(ResourceLocation location) {
        return this.registry.getOrDefault(location, null);
    }

    public Collection<ConfiguredTemperatureEffect<?>> getAllEffects() {
        return this.registry.values();
    }

    public record EntityTypeCacheEntry(ResourceLocation location, ConfiguredTemperatureEffect<?> effect) {
        public EntityTypeCacheEntry(Map.Entry<ResourceLocation, ConfiguredTemperatureEffect<?>> mapEntry) {
            this(mapEntry.getKey(), mapEntry.getValue());
        }
    }

    void updateRegistry(Map<ResourceLocation, ConfiguredTemperatureEffect<?>> effectsRegistry) {
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