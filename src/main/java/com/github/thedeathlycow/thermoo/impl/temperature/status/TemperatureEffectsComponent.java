package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.component.ThermooComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class TemperatureEffectsComponent implements Component, ServerTickingComponent {
    private final Map<ResourceKey<TemperatureStatus>, Settings> effectsSettings = new IdentityHashMap<>();

    private final LivingEntity provider;

    public TemperatureEffectsComponent(LivingEntity provider) {
        this.provider = provider;
    }

    public static TemperatureEffectsComponent get(LivingEntity entity) {
        return ThermooComponents.TEMPERATURE_EFFECTS.get(entity);
    }

    @Nullable
    public static TemperatureEffectsComponent getNullable(Entity entity) {
        return ThermooComponents.TEMPERATURE_EFFECTS.getNullable(entity);
    }

    public boolean setEffectEnabled(ResourceKey<TemperatureStatus> key, boolean enabled) {
        Settings settings = this.effectsSettings.get(key);

        if (settings != null && settings.enabled != enabled) {
            settings.enabled = enabled;
            return true;
        }

        return false;
    }

    public boolean isEffectEnabled(ResourceKey<TemperatureStatus> key) {
        Settings settings = this.effectsSettings.get(key);

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
        Level level = provider.level();
        HolderLookup<TemperatureStatus> statusLookup = level.holderLookup(ThermooRegistryKeys.TEMPERATURE_STATUS);
        List<Holder.Reference<TemperatureStatus>> availableEffects = TemperatureStatusManager.getEffects(
                provider,
                statusLookup
        );

        for (Holder.Reference<TemperatureStatus> effectReference : availableEffects) {
            Settings settings = this.effectsSettings.computeIfAbsent(
                    effectReference.key(),
                    _ -> new Settings()
            );
            boolean wasApplied = settings.applied;
            TemperatureStatus status = effectReference.value();

            if (settings.enabled && status.apply(provider, level)) {
                settings.applied = true;
            } else {
                settings.applied = false;
            }

            if (wasApplied && !settings.applied) {
                status.remove(provider, level);
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

        public static final Codec<Map<ResourceKey<TemperatureStatus>, Settings>> MAP_CODEC = Codec.unboundedMap(
                ResourceKey.codec(ThermooRegistryKeys.TEMPERATURE_STATUS),
                CODEC
        );

        public static final String SETTINGS_KEY = "settings";

        private boolean applied = false;
        private boolean enabled = true;
    }
}