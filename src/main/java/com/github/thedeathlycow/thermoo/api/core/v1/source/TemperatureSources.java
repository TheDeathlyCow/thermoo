package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.resources.ResourceKey;

/**
 * Builtin temperature source types.
 */
public final class TemperatureSources {
    /**
     * The fallback temperature source. Should be used to apply temperature sources absolutely with no resistance.
     * <p>
     * This temperature source may never be ticked by {@link com.github.thedeathlycow.thermoo.api.core.v1.event.LivingEntityTemperatureTickEvents}.
     */
    public static final ResourceKey<TemperatureSource> ABSOLUTE = key("absolute");

    /**
     * The active source should be used for temperature changes from entity effects such as heat from being on fire, or
     * cold from being submerged in powder snow.
     */
    public static final ResourceKey<TemperatureSource> ACTIVE = key("active");

    /**
     * The Passive source should be used for temperature changes from nearby blocks, such as heat from light sources or
     * cooling from an air conditioner.
     */
    public static final ResourceKey<TemperatureSource> PASSIVE = key("passive");

    /**
     * Used by {@link com.github.thedeathlycow.thermoo.api.environment.v2.event.ServerPlayerEnvironmentTickEvents} as the
     * source for temperature changes sourced from the environmental conditions.
     * <p>
     * This temperature source may never be ticked by {@link com.github.thedeathlycow.thermoo.api.core.v1.event.LivingEntityTemperatureTickEvents}.
     * Tick-related logic for this source should be exclusively handled through {@link com.github.thedeathlycow.thermoo.api.environment.v2.event.ServerPlayerEnvironmentTickEvents}.
     */
    public static final ResourceKey<TemperatureSource> ENVIRONMENT = key("environment");

    private static ResourceKey<TemperatureSource> key(String id) {
        return ResourceKey.create(ThermooRegistryKeys.TEMPERATURE_SOURCE, Thermoo.id(id));
    }

    private TemperatureSources() {

    }
}
