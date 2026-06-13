package com.github.thedeathlycow.thermoo.impl.ecs;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

public class Settings {
    public static final Codec<Settings> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL
                            .fieldOf("enabled")
                            .forGetter(Settings::enabled)
            ).apply(instance, Settings::new)
    );

    public static final Codec<Map<ResourceKey<TemperatureStatus>, Settings>> MAP_CODEC = Codec.unboundedMap(
            ResourceKey.codec(ThermooRegistries.TEMPERATURE_STATUS),
            CODEC
    );

    public static final String SETTINGS_KEY = "settings";

    private boolean applied = false;
    private boolean enabled;

    public Settings(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean applied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}