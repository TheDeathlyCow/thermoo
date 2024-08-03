package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

public class TemperatureEffectLoader implements SimpleSynchronousResourceReloadListener {

    public static final TemperatureEffectLoader INSTANCE = Util.make(
            new TemperatureEffectLoader(Thermoo.id("temperature_effects")),
            loader -> {
                ServerLifecycleEvents.SERVER_STARTING.register(server -> loader.clearCache());
                ServerLifecycleEvents.SERVER_STOPPING.register(server -> loader.clearCache());
                ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> loader.clearCache());
            }
    );

    private final Map<Identifier, ConfiguredTemperatureEffect<?>> registry = new HashMap<>();

    private final Map<RegistryKey<EntityType<?>>, Set<ConfiguredTemperatureEffect<?>>> entityTypeCache = new IdentityHashMap<>();

    private final Identifier id;

    public TemperatureEffectLoader(Identifier id) {
        this.id = id;
    }

    public Collection<ConfiguredTemperatureEffect<?>> getEffectsForEntity(LivingEntity entity) {
        EntityType<?> type = entity.getType();

        @SuppressWarnings("deprecation")
        RegistryEntry.Reference<EntityType<?>> entry = type.getRegistryEntry();

        RegistryKey<EntityType<?>> key = entry.registryKey();

        return this.entityTypeCache.computeIfAbsent(
                key,
                ignored -> {
                    if (Thermoo.LOGGER.isDebugEnabled()) {
                        Thermoo.LOGGER.debug("Computing temperature effects for {}", key);
                    }
                    return TemperatureEffects.getLoadedConfiguredEffects()
                            .stream()
                            .filter(configuredEffect -> {
                                var allowedTypes = configuredEffect.entityTypes();
                                return allowedTypes.size() == 0 || type.isIn(allowedTypes);
                            })
                            .collect(Collectors.toSet());
                }
        );
    }

    public Collection<ConfiguredTemperatureEffect<?>> getLoadedConfiguredEffects() {
        return this.registry.values();
    }

    @Override
    public Identifier getFabricId() {
        return this.id;
    }

    @Override
    public void reload(ResourceManager manager) {

        Map<Identifier, ConfiguredTemperatureEffect<?>> updatedRegistry = new HashMap<>();

        Map<Identifier, List<Resource>> entries = manager.findAllResources(
                "thermoo/temperature_effect",
                eid -> eid.getPath().endsWith(".json")
        );

        for (var entry : entries.entrySet()) {
            Identifier key = entry.getKey();
            for (var resource : entry.getValue()) {
                try (BufferedReader reader = resource.getReader()) {
                    this.loadEffect(updatedRegistry, key, reader);
                } catch (Exception e) {
                    Thermoo.LOGGER.error("An error occurred while loading temperature effect {}: {}", entry.getKey(), e);
                }
            }
        }

        this.registry.clear();
        this.registry.putAll(updatedRegistry);

        int numEffects = this.registry.size();
        Thermoo.LOGGER.info("Loaded {} temperature effect{}", numEffects, numEffects == 1 ? "" : "s");
    }

    private void loadEffect(
            Map<Identifier, ConfiguredTemperatureEffect<?>> updatedRegistry,
            Identifier id,
            BufferedReader reader
    ) {
        JsonElement json = JsonParser.parseReader(reader);
        if (json.isJsonObject() && this.objectMatchesConditions(id, json.getAsJsonObject())) {
            ConfiguredTemperatureEffect<?> effect = ConfiguredTemperatureEffect.CODEC.decode(
                    JsonOps.INSTANCE,
                    json
            ).getOrThrow().getFirst();

            boolean overridden = false;
            if (updatedRegistry.containsKey(id)) {
                ConfiguredTemperatureEffect<?> existingEffect = updatedRegistry.get(id);
                if (existingEffect.loadingPriority() > effect.loadingPriority()) {
                    overridden = true;
                }
            }

            if (!overridden) {
                updatedRegistry.put(id, effect);
            } else {
                Thermoo.LOGGER.info("Temperature Effect {} tried to load, but was overridden by a higher priority effect with the same ID.", id);
            }
        } else {
            Thermoo.LOGGER.info("Temperature Effect {} not loaded, as its resource conditions were not met.", id);
        }
    }

    private boolean objectMatchesConditions(Identifier key, JsonObject json) {
        if (json.has(ResourceConditions.CONDITIONS_KEY)) {
            DataResult<ResourceCondition> conditions = ResourceCondition.CONDITION_CODEC.parse(
                    JsonOps.INSTANCE,
                    json.get(ResourceConditions.CONDITIONS_KEY)
            );

            if (conditions.isSuccess()) {
                boolean matched = conditions.getOrThrow().test(null);

                if (Thermoo.LOGGER.isDebugEnabled()) {
                    String verdict = matched ? "Allowed" : "Rejected";
                    Thermoo.LOGGER.debug(
                            "{} resource of type {} with id {}",
                            verdict,
                            RegistryKeys.getPath(ThermooRegistryKeys.TEMPERATURE_EFFECT),
                            key
                    );
                }

                return matched;
            } else {
                Thermoo.LOGGER.error(
                        "Failed to parse resource conditions for file of type {} with id {}, skipping: {}",
                        RegistryKeys.getPath(ThermooRegistryKeys.TEMPERATURE_EFFECT),
                        key,
                        conditions.error().get().message()
                );
            }
        }

        return true;
    }

    private void clearCache() {
        this.entityTypeCache.clear();
        Thermoo.LOGGER.info("Clearing Entity Type to Temperature Effect cache");
    }
}
