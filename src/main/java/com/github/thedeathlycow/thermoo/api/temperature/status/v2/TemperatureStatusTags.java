package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.tags.TagKey;

/**
 * Builtin tag keys for {@linkplain TemperatureStatus temperature statuses}.
 */
public final class TemperatureStatusTags {
    /**
     * Specifies the order in which temperature statuses are applied to an entity.
     * <p>
     * Statuses included in this tag are processed first, following the order defined in the tag. Any statuses not
     * present in this tag are processed afterwards in an undefined order.
     */
    public static final TagKey<TemperatureStatus> APPLICATION_ORDER = create("application_order");

    private static TagKey<TemperatureStatus> create(String name) {
        return TagKey.create(ThermooRegistryKeys.TEMPERATURE_STATUS, Thermoo.id(name));
    }

    private TemperatureStatusTags() {

    }
}