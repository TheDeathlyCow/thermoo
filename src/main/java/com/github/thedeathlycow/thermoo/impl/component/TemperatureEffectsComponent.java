package com.github.thedeathlycow.thermoo.impl.component;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.temperature.effect.TemperatureEffectManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.HashMap;
import java.util.Map;

public class TemperatureEffectsComponent implements Component, ServerTickingComponent {

    private final Map<Identifier, Settings> effectsSettings = new HashMap<>();

    private final LivingEntity provider;

    public TemperatureEffectsComponent(LivingEntity provider) {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {

    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {

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