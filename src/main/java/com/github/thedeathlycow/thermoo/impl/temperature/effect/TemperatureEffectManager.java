package com.github.thedeathlycow.thermoo.impl.temperature.effect;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class TemperatureEffectManager {

    public static final TemperatureEffectManager INSTANCE = new TemperatureEffectManager();

    private final Map<RegistryKey<EntityType<?>>, Set<EntityTypeCacheEntry>> entityTypeCache = new IdentityHashMap<>();

    private final Map<Identifier, ConfiguredTemperatureEffect<?>> registry = new HashMap<>();

    /**
     * @deprecated use {@link #getEffectsEntriesForEntity(LivingEntity)}
     */
    @Deprecated
    public Collection<ConfiguredTemperatureEffect<?>> getEffectsForEntity(LivingEntity entity) {
        return getEffectsEntriesForEntity(entity)
                .stream()
                .map(EntityTypeCacheEntry::effect)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<EntityTypeCacheEntry> getEffectsEntriesForEntity(LivingEntity entity) {
        EntityType<?> type = entity.getType();

        RegistryEntry.Reference<EntityType<?>> entityTypeEntry = type.getRegistryEntry();
        RegistryKey<EntityType<?>> entityTypeKey = entityTypeEntry.registryKey();

        return this.entityTypeCache.computeIfAbsent(
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
    }

    public ConfiguredTemperatureEffect<?> getEffect(Identifier id) {
        return this.registry.getOrDefault(id, null);
    }

    public Collection<ConfiguredTemperatureEffect<?>> getAllEffects() {
        return this.registry.values();
    }

    public record EntityTypeCacheEntry(Identifier id, ConfiguredTemperatureEffect<?> effect) {
        public EntityTypeCacheEntry(Map.Entry<Identifier, ConfiguredTemperatureEffect<?>> mapEntry) {
            this(mapEntry.getKey(), mapEntry.getValue());
        }
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