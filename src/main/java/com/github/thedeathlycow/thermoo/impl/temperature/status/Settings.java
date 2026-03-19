package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

class Settings {
    static final Codec<Settings> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL
                            .fieldOf("enabled")
                            .forGetter(Settings::enabled)
            ).apply(instance, Settings::new)
    );

    static final Codec<Map<ResourceKey<TemperatureStatus>, Settings>> MAP_CODEC = Codec.unboundedMap(
            ResourceKey.codec(ThermooRegistryKeys.TEMPERATURE_STATUS),
            CODEC
    );

    static final String SETTINGS_KEY = "settings";

    private boolean applied = false;
    private boolean enabled;

    Settings(boolean enabled) {
        this.enabled = enabled;
    }

    boolean applied() {
        return applied;
    }

    void setApplied(boolean applied) {
        this.applied = applied;
    }

    boolean enabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}