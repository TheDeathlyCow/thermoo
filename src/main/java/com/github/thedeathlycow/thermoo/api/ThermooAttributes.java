package com.github.thedeathlycow.thermoo.api;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;

/**
 * Custom {@link EntityAttribute}s provided by Thermoo
 */
public final class ThermooAttributes {


    /**
     * The minimum temperature of an entity
     */
    public static final EntityAttribute MIN_TEMPERATURE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.min_temperature", 0.0, 0.0, 8192
    ).setTracked(false);

    /**
     * The maximum temperature of an entity
     */
    public static final EntityAttribute MAX_TEMPERATURE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.max_temperature", 0.0, 0.0, 8192
    ).setTracked(false);

    /**
     * The cold resistance of an entity. 1 point of frost resistance corresponds to a 10% cold reduction
     */
    public static final EntityAttribute FROST_RESISTANCE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.frost_resistance", 0.0, -10.0, 10.0
    ).setTracked(false);

    /**
     * The heat resistance of an entity. 1 point of heat resistance corresponds to a 10% heat reduction
     */
    public static final EntityAttribute HEAT_RESISTANCE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.heat_resistance", 0.0, -10.0, 10.0
    ).setTracked(false);

}
