package com.github.thedeathlycow.thermoo.impl.component;

import com.github.thedeathlycow.thermoo.api.temperature.effects.ConfiguredTemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.temperature.effect.TemperatureEffectManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.HashMap;
import java.util.Map;

public class TemperatureEffectsComponent implements Component, ServerTickingComponent {

    private Map<Identifier, Settings> effectsSettings = new HashMap<>();

    private final LivingEntity provider;

    public TemperatureEffectsComponent(LivingEntity provider) {
        this.provider = provider;
    }

    public boolean setEffectEnabled(Identifier id, boolean enabled) {
        Settings settings = this.effectsSettings.get(id);

        if (settings != null) {
            settings.enabled = enabled;
            return true;
        }

        return false;
    }

    @Override
    public void readData(ValueInput readView) {
        readView.read(Settings.SETTINGS_KEY, Settings.MAP_CODEC).ifPresentOrElse(
                settings -> this.effectsSettings = settings,
                () -> this.effectsSettings = new HashMap<>()
        );
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.store(Settings.SETTINGS_KEY, Settings.MAP_CODEC, this.effectsSettings);
    }

    @Override
    public void serverTick() {
        var availableEffects = TemperatureEffectManager.INSTANCE.getEffectsEntriesForEntity(provider);
        for (TemperatureEffectManager.EntityTypeCacheEntry effectEntry : availableEffects) {
            Settings settings = this.effectsSettings.computeIfAbsent(effectEntry.location(), ignored -> new Settings());
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