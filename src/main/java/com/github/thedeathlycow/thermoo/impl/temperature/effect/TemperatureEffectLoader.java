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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;

import java.io.BufferedReader;
import java.util.*;

public class TemperatureEffectLoader implements SimpleSynchronousResourceReloadListener {

    public static final String DIRECTORY = "thermoo/temperature_effect";
    public static final Identifier ID = Thermoo.id("temperature_effects");

    private final Map<Identifier, ConfiguredTemperatureEffect<?>> globalEffects = new HashMap<>();

    private final Map<ResourceKey<EntityType<?>>, Set<ConfiguredTemperatureEffect<?>>> entityTypeToEffect = new IdentityHashMap<>();

    private final RegistryOps<JsonElement> ops;

    public TemperatureEffectLoader(HolderLookup.Provider lookup) {
        this.ops = RegistryOps.create(JsonOps.INSTANCE, lookup);
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        Map<Identifier, ConfiguredTemperatureEffect<?>> updatedRegistry = new HashMap<>();
        FileToIdConverter resourceFinder = FileToIdConverter.json(DIRECTORY);
        Map<Identifier, List<Resource>> foundResources = resourceFinder.listMatchingResourceStacks(manager);

        for (Map.Entry<Identifier, List<Resource>> allResources : foundResources.entrySet()) {
            Identifier effectID = resourceFinder.fileToId(allResources.getKey());
            for (Resource resource : allResources.getValue()) {
                try (BufferedReader reader = resource.openAsReader()) {
                    this.loadEffect(updatedRegistry, effectID, reader);
                } catch (Exception e) {
                    Thermoo.LOGGER.error("An error occurred while loading temperature effect {}: {}", allResources.getKey(), e);
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
                            Registries.elementsDirPath(ThermooRegistryKeys.TEMPERATURE_EFFECT),
                            key
                    );
                }

                return matched;
            } else {
                Thermoo.LOGGER.error(
                        "Failed to parse resource conditions for file of type {} with id {}, skipping: {}",
                        Registries.elementsDirPath(ThermooRegistryKeys.TEMPERATURE_EFFECT),
                        key,
                        conditions.error().get().message()
                );
            }
        }

        return true;
    }
}
