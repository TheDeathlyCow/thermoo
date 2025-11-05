package com.github.thedeathlycow.thermoo.impl.component;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.temperature.effect.TemperatureEffectManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
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

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {

    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {

    }

    @Override
    public void serverTick() {
        var availableEffects = TemperatureEffectManager.INSTANCE.getEffectsEntriesForEntity(provider);
        for (TemperatureEffectManager.EntityTypeCacheEntry effectEntry : availableEffects) {
            Settings settings = this.effectsSettings.computeIfAbsent(effectEntry.id(), ignored -> new Settings());
            boolean wasApplied = settings.applied;
            ConfiguredTemperatureEffect<?> effect = effectEntry.effect();

            if (effect.apply(provider)) {
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
        private boolean applied = false;
    }
}