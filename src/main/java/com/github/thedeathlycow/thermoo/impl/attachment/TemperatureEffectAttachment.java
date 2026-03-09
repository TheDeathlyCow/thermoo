package com.github.thedeathlycow.thermoo.impl.attachment;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.temperature.effect.TemperatureEffectManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class TemperatureEffectAttachment implements INBTSerializable<CompoundTag> {
    private final Map<ResourceLocation, Settings> effectsSettings = new HashMap<>();

    private final IAttachmentHolder provider;

    public TemperatureEffectAttachment(IAttachmentHolder provider) {
        this.provider = provider;
    }

    public boolean setEffectEnabled(ResourceLocation effectId, boolean enabled) {
        if (!(this.provider instanceof LivingEntity providerEntity)) {
            return false;
        }

        Settings settings = this.getSettingsChecked(effectId);

        if (settings != null && settings.enabled() != enabled) {
            settings.setEnabled(enabled);

            // this is meant to ensure that the effect is cleaned up right away and not have to wait for the next
            // interval check, especially if that interval is long.
            if (!settings.enabled() && settings.applied()) {
                ConfiguredTemperatureEffect<?> effect = TemperatureEffectManager.INSTANCE.getEffect(effectId);
                if (effect != null) {
                    effect.remove(providerEntity);
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
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var tag = new CompoundTag();

        Settings.MAP_CODEC.encodeStart(NbtOps.INSTANCE, this.effectsSettings).ifSuccess(serializedMap -> {
            tag.put(Settings.SETTINGS_KEY, serializedMap);
        });

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.effectsSettings.clear();

        Settings.MAP_CODEC.decode(NbtOps.INSTANCE, tag).ifSuccess(result -> {
            this.effectsSettings.putAll(result.getFirst());
        });
    }

    public void serverTick() {
        if (!(this.provider instanceof LivingEntity providerEntity)) {
            return;
        }

        var availableEffects = TemperatureEffectManager.INSTANCE.getEffectsEntriesForEntity(providerEntity);
        for (TemperatureEffectManager.EntityTypeCacheEntry effectEntry : availableEffects) {
            ConfiguredTemperatureEffect<?> effect = effectEntry.effect();
            Settings settings = this.getSettings(effectEntry.id());
            this.updateStatus(effect, settings);
        }
    }

    private void updateStatus(ConfiguredTemperatureEffect<?> effect, Settings settings) {
        if (settings.enabled()) {
            if (!(this.provider instanceof LivingEntity providerEntity)) {
                return;
            }

            boolean wasApplied = settings.applied();
            boolean applied = effect.apply(providerEntity);

            if (wasApplied && !applied) {
                effect.remove(providerEntity);
            }

            settings.setApplied(applied);
        }
    }

    @Nullable
    private Settings getSettingsChecked(ResourceLocation effectId) {
        if (!(this.provider instanceof LivingEntity providerEntity)) {
            return null;
        }

        ConfiguredTemperatureEffect<?> effect = TemperatureEffectManager.INSTANCE.getEffect(effectId);

        if (effect != null && (effect.entityTypes().size() == 0 || effect.entityTypes().contains(providerEntity.getType().builtInRegistryHolder()))) {
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