package com.github.thedeathlycow.thermoo.impl.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

class Settings {
    public static final Codec<Settings> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL
                            .fieldOf("enabled")
                            .forGetter(settings -> settings.enabled)
            ).apply(instance, Settings::new)
    );

    public static final Codec<Map<ResourceLocation, Settings>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, CODEC);

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