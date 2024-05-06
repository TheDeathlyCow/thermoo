package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.BufferedReader;
import java.util.*;

public class TemperatureEffectLoader implements SimpleSynchronousResourceReloadListener {

    public static final TemperatureEffectLoader INSTANCE = new TemperatureEffectLoader(Thermoo.id("temperature_effects"));

    private final Map<Identifier, ConfiguredTemperatureEffect<?>> globalEffects = new HashMap<>();

    private final Map<Identifier, Set<ConfiguredTemperatureEffect<?>>> typeSpecificEffects = new HashMap<>();

    private final Identifier id;

    public TemperatureEffectLoader(Identifier id) {
        this.id = id;
    }

    public Collection<ConfiguredTemperatureEffect<?>> getEffectsForEntity(LivingEntity entity) {
        EntityType<?> type = entity.getType();

        Identifier typeId = Registries.ENTITY_TYPE.getId(type);

        Set<ConfiguredTemperatureEffect<?>> effects = typeSpecificEffects.get(typeId);

        if (effects == null) {
            return Collections.emptySet();
        }

        return effects;
    }

    public Collection<ConfiguredTemperatureEffect<?>> getGlobalEffects() {
        return globalEffects.values();
    }

    @Override
    public Identifier getFabricId() {
        return this.id;
    }

    @Override
    public void reload(ResourceManager manager) {

        Map<Identifier, ConfiguredTemperatureEffect<?>> registry = new HashMap<>();

        for (Map.Entry<Identifier, Resource> entry : manager.findResources("thermoo/temperature_effects", eid -> eid.getPath().endsWith(".json")).entrySet()) {
            try (BufferedReader reader = entry.getValue().getReader()) {
                this.loadEffect(registry, entry.getKey(), reader);
            } catch (Exception e) {
                Thermoo.LOGGER.error("An error occurred while loading temperature effect {}: {}", entry.getKey(), e);
            }
        }

        Map<Identifier, ConfiguredTemperatureEffect<?>> newEffects = new HashMap<>();
        Map<Identifier, Set<ConfiguredTemperatureEffect<?>>> newTypeEffects = new HashMap<>();
        this.partitionRegistry(registry, newEffects, newTypeEffects);

        this.globalEffects.clear();
        this.globalEffects.putAll(newEffects);
        this.typeSpecificEffects.clear();
        this.typeSpecificEffects.putAll(newTypeEffects);

        int numEffects = this.globalEffects.size();
        int numTypeEffects = this.typeSpecificEffects.values()
                .stream()
                .map(Set::size)
                .reduce(0, Integer::sum);
        Thermoo.LOGGER.info("Loaded {} global temperature effect{}", numEffects, numEffects == 1 ? "" : "s");
        Thermoo.LOGGER.info("Loaded {} type specific temperature effect{}", numTypeEffects, numTypeEffects == 1 ? "" : "s");
    }

    private void loadEffect(
            Map<Identifier, ConfiguredTemperatureEffect<?>> registry,
            Identifier id,
            BufferedReader reader
    ) {
        JsonElement json = JsonParser.parseReader(reader);
        if (json.isJsonObject() && ResourceConditions.objectMatchesConditions(json.getAsJsonObject())) {
            ConfiguredTemperatureEffect<?> effect = Util.getResult(
                    ConfiguredTemperatureEffect.CODEC.parse(JsonOps.INSTANCE, json),
                    JsonParseException::new
            );
            registry.put(id, effect);
        } else {
            Thermoo.LOGGER.info("Temperature Effect {} not loaded, as resource conditions not met.", id);
        }
    }

    private void partitionRegistry(
            Map<Identifier, ConfiguredTemperatureEffect<?>> registry,
            Map<Identifier, ConfiguredTemperatureEffect<?>> globalEffects,
            Map<Identifier, Set<ConfiguredTemperatureEffect<?>>> typeEffects
    ) {
        registry.forEach((key, value) -> {
            value.entityType().ifPresentOrElse(
                    entityType -> {
                        typeEffects.computeIfAbsent(
                                Registries.ENTITY_TYPE.getId(entityType),
                                eid -> new HashSet<>()
                        ).add(value);
                    },
                    () -> globalEffects.put(key, value)
            );
        });
    }
}
