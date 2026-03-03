package com.github.thedeathlycow.thermoo.impl.attachment;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.temperature.effect.TemperatureEffectManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class TemperatureEffectAttachment {
    private final Map<ResourceLocation, Settings> effectsSettings = new HashMap<>();

    public void serverTick(LivingEntity provider) {
    private final LivingEntity provider;

    public TemperatureEffectsComponent(LivingEntity provider) {
        this.provider = provider;
    }

    public boolean setEffectEnabled(ResourceLocation id, boolean enabled) {
        Settings settings = this.effectsSettings.get(id);

        if (settings != null && settings.enabled != enabled) {
            settings.enabled = enabled;
            return true;
        }

        return false;
    }

    public boolean isEffectEnabled(ResourceLocation id) {
        Settings settings = this.effectsSettings.get(id);

        if (settings != null) {
            return settings.enabled;
        }

        return false;
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider provider) {
        this.effectsSettings.clear();

        Settings.MAP_CODEC.decode(NbtOps.INSTANCE, tag).ifSuccess(result -> {
            this.effectsSettings.putAll(result.getFirst());
        });
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider provider) {
        Settings.MAP_CODEC.encodeStart(NbtOps.INSTANCE, this.effectsSettings).ifSuccess(serializedMap -> {
            tag.put(Settings.SETTINGS_KEY, serializedMap);
        });
    }

    @Override
    public void serverTick() {
        var availableEffects = TemperatureEffectManager.INSTANCE.getEffectsEntriesForEntity(provider);
        for (TemperatureEffectManager.EntityTypeCacheEntry effectEntry : availableEffects) {
            Settings settings = this.effectsSettings.computeIfAbsent(effectEntry.id(), ignored -> new Settings());
            boolean wasApplied = settings.applied;
            ConfiguredTemperatureEffect<?> effect = effectEntry.effect();

            if (settings.enabled && effect.apply(provider)) {
                settings.applied = true;
            } else {
                settings.applied = false;
            }

            if (wasApplied && !settings.applied) {
                effect.remove(provider);
            }
        }
    }

    private static class Settings {
        public static final Codec<Settings> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.BOOL
                                .fieldOf("enabled")
                                .forGetter(settings -> settings.enabled)
                ).apply(instance, enabled -> {
                    var settings = new Settings();
                    settings.enabled = enabled;
                    return settings;
                })
        );

        public static final Codec<Map<ResourceLocation, Settings>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, CODEC);

        public static final String SETTINGS_KEY = "settings";

        private boolean applied = false;
        private boolean enabled = true;
    }
}