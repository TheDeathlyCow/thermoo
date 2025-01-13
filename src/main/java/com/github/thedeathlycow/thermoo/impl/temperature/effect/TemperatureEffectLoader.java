package com.github.thedeathlycow.thermoo.impl.temperature.effect;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.util.*;

public class TemperatureEffectLoader implements SimpleSynchronousResourceReloadListener {

    public static final Identifier ID = Thermoo.id("temperature_effects");

    private final Map<Identifier, ConfiguredTemperatureEffect<?>> globalEffects = new HashMap<>();

    private final Map<RegistryKey<EntityType<?>>, Set<ConfiguredTemperatureEffect<?>>> entityTypeToEffect = new IdentityHashMap<>();

    private final RegistryOps<JsonElement> ops;

    public TemperatureEffectLoader(RegistryWrapper.WrapperLookup lookup) {
        this.ops = RegistryOps.of(JsonOps.INSTANCE, lookup);
    }

    @Override
    public Identifier getFabricId() {
        return ID;
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
        TemperatureEffectManager.INSTANCE.updateRegistry(updatedRegistry);
    }

    private void loadEffect(
            Map<Identifier, ConfiguredTemperatureEffect<?>> updatedRegistry,
            Identifier id,
            BufferedReader reader
    ) {
        JsonElement json = JsonParser.parseReader(reader);
        if (json.isJsonObject() && this.objectMatchesConditions(id, json.getAsJsonObject())) {
            ConfiguredTemperatureEffect<?> effect = ConfiguredTemperatureEffect.CODEC.decode(this.ops, json)
                    .getOrThrow()
                    .getFirst();

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
                Thermoo.LOGGER.debug("Temperature Effect {} tried to load, but was overridden by a higher priority effect with the same ID.", id);
            }
        } else {
            Thermoo.LOGGER.debug("Temperature Effect {} not loaded, as its resource conditions were not met.", id);
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

    private void partitionRegistry(
            Map<Identifier, ConfiguredTemperatureEffect<?>> registry,
            Map<Identifier, ConfiguredTemperatureEffect<?>> globalEffects,
            Map<RegistryKey<EntityType<?>>, Set<ConfiguredTemperatureEffect<?>>> typeEffects
    ) {
        registry.forEach((key, value) -> {
            value.entityType().ifPresentOrElse(
                    entityType -> {
                        typeEffects.computeIfAbsent(
                                entityType.getRegistryEntry().registryKey(),
                                eid -> new HashSet<>()
                        ).add(value);
                    },
                    () -> globalEffects.put(key, value)
            );
        });
    }
}
