package com.github.thedeathlycow.thermoo.api.temperature.status.v2.tag;

import com.github.thedeathlycow.thermoo.api.core.v1.registry.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
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

    /**
     * Statuses that are harmful to their targets.
     */
    public static final TagKey<TemperatureStatus> HARMFUL = create("harmful");

    /**
     * Statuses that are beneficial to their targets.
     */
    public static final TagKey<TemperatureStatus> BENEFICIAL = create("beneficial");

    /**
     * Statuses that are neither harmful nor beneficial.
     */
    public static final TagKey<TemperatureStatus> NEUTRAL = create("neutral");

    /**
     * Statuses that are applied as a result of being {@link TemperatureAware#thermoo$isCold() cold}.
     */
    public static final TagKey<TemperatureStatus> COLD = create("cold");

    /**
     * Statuses that are applied as a result of being {@link TemperatureAware#thermoo$isWarm() warm}.
     */
    public static final TagKey<TemperatureStatus> WARM = create("warm");

    private static TagKey<TemperatureStatus> create(String name) {
        return TagKey.create(ThermooRegistryKeys.TEMPERATURE_STATUS, Thermoo.id(name));
    }

    private TemperatureStatusTags() {

    }
}