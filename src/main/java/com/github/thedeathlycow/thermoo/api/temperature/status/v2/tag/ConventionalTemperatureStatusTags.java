package com.github.thedeathlycow.thermoo.api.temperature.status.v2.tag;

import com.github.thedeathlycow.thermoo.api.core.v1.registry.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

/**
 * Conventional version of the tags defined in {@link TemperatureStatusTags} provided for those who prefer to use the
 * common 'c' namespace. Note that not all tags are aliased here, only the ones that exist exclusively for conventional
 * purposes.
 */
public final class ConventionalTemperatureStatusTags {
    /**
     * Conventional alias of {@link TemperatureStatusTags#HARMFUL}
     */
    public static final TagKey<TemperatureStatus> HARMFUL = create("harmful");

    /**
     * Conventional alias of {@link TemperatureStatusTags#BENEFICIAL}
     */
    public static final TagKey<TemperatureStatus> BENEFICIAL = create("beneficial");

    /**
     * Conventional alias of {@link TemperatureStatusTags#NEUTRAL}
     */
    public static final TagKey<TemperatureStatus> NEUTRAL = create("neutral");

    /**
     * Conventional alias of {@link TemperatureStatusTags#COLD}
     */
    public static final TagKey<TemperatureStatus> COLD = create("cold");

    /**
     * Conventional alias of {@link TemperatureStatusTags#WARM}
     */
    public static final TagKey<TemperatureStatus> WARM = create("warm");

    private static TagKey<TemperatureStatus> create(String name) {
        return TagKey.create(ThermooRegistryKeys.TEMPERATURE_STATUS, Identifier.fromNamespaceAndPath("c", name));
    }

    private ConventionalTemperatureStatusTags() {

    }
}