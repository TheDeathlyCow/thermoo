package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.tags.TagKey;

public final class TemperatureStatusTags {
    public static final TagKey<TemperatureStatus> APPLICATION_ORDER = create("application_order");

    private static TagKey<TemperatureStatus> create(String name) {
        return TagKey.create(ThermooRegistryKeys.TEMPERATURE_STATUS, Thermoo.id(name));
    }

    private TemperatureStatusTags() {

    }
}