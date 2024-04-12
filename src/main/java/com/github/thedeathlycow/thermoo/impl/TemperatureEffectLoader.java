package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
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
        Map<Identifier, ConfiguredTemperatureEffect<?>> newEffects = new HashMap<>();
        Map<Identifier, Set<ConfiguredTemperatureEffect<?>>> newTypeEffects = new HashMap<>();

        for (Map.Entry<Identifier, Resource> entry : manager.findResources("thermoo/temperature_effects", eid -> eid.getPath().endsWith(".json")).entrySet()) {
            try (BufferedReader reader = entry.getValue().getReader()) {
                this.loadEffect(newEffects, newTypeEffects, entry, reader);
            } catch (Exception e) {
                Thermoo.LOGGER.error("An error occurred while loading temperature effect {}: {}", entry.getKey(), e);
            }
        }

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
            Map<Identifier, ConfiguredTemperatureEffect<?>> newEffects,
            Map<Identifier, Set<ConfiguredTemperatureEffect<?>>> newTypeEffects,
            Map.Entry<Identifier, Resource> entry,
            BufferedReader reader
    ) {
        ConfiguredTemperatureEffect<?> effect = Util.getResult(
                ConfiguredTemperatureEffect.CODEC.parse(
                        JsonOps.INSTANCE,
                        JsonParser.parseReader(reader)
                ),
                JsonParseException::new
        );

        EntityType<?> type = effect.entityType().orElse(null);
        if (type != null) {
            newTypeEffects.computeIfAbsent(
                    Registries.ENTITY_TYPE.getId(type),
                    eid -> new HashSet<>()
            ).add(effect);
        } else {
            newEffects.put(entry.getKey(), effect);
        }
    }
}
