package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusSelector;
import com.github.thedeathlycow.thermoo.impl.component.ThermooComponents;
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

    public boolean setEffectEnabled(Holder.Reference<TemperatureStatus> statusRef, boolean enabled) {
        Settings settings = this.getSettingsChecked(statusRef);

        if (settings != null && settings.enabled() != enabled) {
            settings.setEnabled(enabled);

            // this is meant to ensure that the effect is cleaned up right away and not have to wait for the next
            // interval check, especially if that interval is long.
            if (!settings.enabled() && settings.applied()) {
                ((TemperatureStatusImpl) statusRef.value()).remove(this.provider, TemperatureEffectContextImpl.INSTANCE);
                settings.setApplied(false);
            }

            return true;
        }
        return false;
    }

    public boolean isEffectEnabled(Holder.Reference<TemperatureStatus> statusRef) {
        Settings settings = this.getSettingsChecked(statusRef);

        if (settings != null) {
            return settings.enabled();
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
        List<Holder.Reference<TemperatureStatus>> possibleStatuses = TemperatureStatusManager.getEffects(
                provider,
                statusLookup
        );

        for (Holder.Reference<TemperatureStatus> statusRef : possibleStatuses) {
            TemperatureStatusImpl status = (TemperatureStatusImpl) statusRef.value();

            if (provider.tickCount % status.interval() != 0) {
                continue;
            }

            Settings settings = this.getSettings(statusRef);
            this.updateStatus(status, settings);
        }
    }

    private void updateStatus(TemperatureStatusImpl status, Settings settings) {
        if (settings.enabled()) {
            boolean wasApplied = settings.applied();
            boolean applied = status.apply(provider, TemperatureEffectContextImpl.INSTANCE);

            if (wasApplied && !applied) {
                status.remove(provider, TemperatureEffectContextImpl.INSTANCE);
            }

            settings.setApplied(applied);
        }
    }

    @Nullable
    private Settings getSettingsChecked(Holder.Reference<TemperatureStatus> statusRef) {
        TemperatureStatusSelector selector = statusRef.value().selector();

        if (selector.appliesToEntityType(this.provider.typeHolder())) {
            return this.getSettings(statusRef);
        } else {
            return null;
        }
    }

    private Settings getSettings(Holder.Reference<TemperatureStatus> statusRef) {
        return this.effectsSettings.computeIfAbsent(
                statusRef.key(),
                _ -> new Settings(statusRef.value().enabledByDefault())
        );
    }
}