package com.github.thedeathlycow.thermoo.impl.neoforge.attachment;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusSelector;
import com.github.thedeathlycow.thermoo.impl.ecs.Settings;
import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSettingsComponent;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public final class TemperatureStatusSettingsAttachment implements TemperatureStatusSettingsComponent, ValueIOSerializable {
    private final Map<ResourceKey<TemperatureStatus>, Settings> value = new IdentityHashMap<>();
    private final LivingEntity provider;

    public TemperatureStatusSettingsAttachment(IAttachmentHolder holder) {
        if (!(holder instanceof LivingEntity providerEntity)) {
            throw new IllegalStateException("Temperature status settings can only be held by a LivingEntity");
        }

        this.provider = providerEntity;
    }

    @Override
    public Map<ResourceKey<TemperatureStatus>, Settings> getSettings() {
        return this.value;
    }

    @Override
    public LivingEntity getProvider() {
        return this.provider;
    }

    @Override
    public @Nullable Settings getSettings(Holder.Reference<TemperatureStatus> statusRef) {
        TemperatureStatusSelector selector = statusRef.value().selector();

        if (selector.appliesToEntityType(this.getProvider().typeHolder())) {
            return this.getSettingsChecked(statusRef);
        } else {
            return null;
        }
    }

    private Settings getSettingsChecked(Holder.Reference<TemperatureStatus> statusRef) {
        return this.value.computeIfAbsent(
                statusRef.key(),
                _ -> new Settings(statusRef.value().enabledByDefault())
        );
    }

    @Override
    public void deserialize(ValueInput readView) {
        this.value.clear();
        readView.read(Settings.SETTINGS_KEY, Settings.MAP_CODEC).ifPresent(this.value::putAll);
    }

    @Override
    public void serialize(ValueOutput writeView) {
        writeView.store(Settings.SETTINGS_KEY, Settings.MAP_CODEC, this.value);
    }
}