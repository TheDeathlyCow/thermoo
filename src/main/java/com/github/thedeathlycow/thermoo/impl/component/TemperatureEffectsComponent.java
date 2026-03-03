package com.github.thedeathlycow.thermoo.impl.component;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.temperature.effect.TemperatureEffectManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.HashMap;
import java.util.Map;

public class TemperatureEffectsComponent implements Component, ServerTickingComponent {

    private final Map<ResourceLocation, Settings> effectsSettings = new HashMap<>();

    private final LivingEntity provider;

    public TemperatureEffectsComponent(LivingEntity provider) {
        this.provider = provider;
    }

    public boolean setEffectEnabled(ResourceLocation effectId, boolean enabled) {
        Settings settings = this.getSettingsChecked(effectId);

        if (settings != null && settings.enabled() != enabled) {
            settings.setEnabled(enabled);

            // this is meant to ensure that the effect is cleaned up right away and not have to wait for the next
            // interval check, especially if that interval is long.
            if (!settings.enabled() && settings.applied()) {
                ConfiguredTemperatureEffect<?> effect = TemperatureEffectManager.INSTANCE.getEffect(effectId);
                if (effect != null) {
                    effect.remove(this.provider);
                    settings.setApplied(false);
                }
            }

            return true;
        }
        return false;
    }

    public boolean isEffectEnabled(ResourceLocation effectId) {
        Settings settings = this.getSettingsChecked(effectId);

        if (settings != null) {
            return settings.enabled();
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
            ConfiguredTemperatureEffect<?> effect = effectEntry.effect();
            Settings settings = this.getSettings(effectEntry.id());
            this.updateStatus(effect, settings);
        }
    }

    private void updateStatus(ConfiguredTemperatureEffect<?> effect, Settings settings) {
        if (settings.enabled()) {
            boolean wasApplied = settings.applied();
            boolean applied = effect.apply(provider);

            if (wasApplied && !applied) {
                effect.remove(provider);
            }

            settings.setApplied(applied);
        }
    }

    @Nullable
    private Settings getSettingsChecked(ResourceLocation effectId) {
        ConfiguredTemperatureEffect<?> effect = TemperatureEffectManager.INSTANCE.getEffect(effectId);

        if (effect != null && (effect.entityTypes().size() == 0 || effect.entityTypes().contains(this.provider.getType().builtInRegistryHolder()))) {
            return this.getSettings(effectId);
        } else {
            return null;
        }
    }

    private Settings getSettings(ResourceLocation effectId) {
        return this.effectsSettings.computeIfAbsent(
                effectId,
                ignored -> new Settings(true)
        );
    }
}