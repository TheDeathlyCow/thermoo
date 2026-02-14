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

    public boolean setEffectEnabled(Identifier id, boolean enabled) {
        Settings settings = this.effectsSettings.get(id);

        if (settings != null && settings.enabled != enabled) {
            settings.enabled = enabled;
            return true;
        }

        return false;
    }

    public boolean isEffectEnabled(Identifier id) {
        Settings settings = this.effectsSettings.get(id);

        if (settings != null) {
            return settings.enabled;
        }

        return false;
    }

    @Override
    public void readData(ValueInput readView) {
        this.effectsSettings.clear();
        readView.read(Settings.SETTINGS_KEY, Settings.MAP_CODEC).ifPresent(this.effectsSettings::putAll);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.store(Settings.SETTINGS_KEY, Settings.MAP_CODEC, this.effectsSettings);
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

        public static final Codec<Map<Identifier, Settings>> MAP_CODEC = Codec.unboundedMap(Identifier.CODEC, CODEC);

        public static final String SETTINGS_KEY = "settings";

        private boolean applied = false;
        private boolean enabled = true;
    }
}