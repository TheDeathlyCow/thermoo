package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TemperatureEffectLoader implements SimpleSynchronousResourceReloadListener {

    public static final TemperatureEffectLoader INSTANCE = new TemperatureEffectLoader(Thermoo.id("temperature_effects"));

    private final Map<Identifier, ConfiguredTemperatureEffect<?>> effects = new HashMap<>();
    private final Identifier id;

    public TemperatureEffectLoader(Identifier id) {
        this.id = id;
    }

    public Collection<ConfiguredTemperatureEffect<?>> getEffects() {
        return effects.values();
    }

    @Override
    public Identifier getFabricId() {
        return this.id;
    }

    @Override
    public void reload(ResourceManager manager) {
        Map<Identifier, ConfiguredTemperatureEffect<?>> newEffects = new HashMap<>();

        for (var entry : manager.findResources("thermoo/temperature_effects", id -> id.getPath().endsWith(".json")).entrySet()) {
            try (BufferedReader reader = entry.getValue().getReader()) {

                ConfiguredTemperatureEffect<?> effect = ConfiguredTemperatureEffect.Serializer.GSON.fromJson(
                        reader,
                        ConfiguredTemperatureEffect.class
                );

                newEffects.put(entry.getKey(), effect);
            } catch (Exception e) {
                Thermoo.LOGGER.error("An error occurred while loading temperature effect {}: {}", entry.getKey(), e);
            }
        }

        this.effects.clear();
        this.effects.putAll(newEffects);

        int numEffects = this.effects.size();
        Thermoo.LOGGER.info("Loaded {} survival effect{}", numEffects, numEffects == 1 ? "" : "s");

    }
}
