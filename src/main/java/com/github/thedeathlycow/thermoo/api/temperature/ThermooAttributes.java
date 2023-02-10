package com.github.thedeathlycow.thermoo.api.temperature;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;

public final class ThermooAttributes {


    public static final EntityAttribute MIN_TEMPERATURE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.min_temperature", 40.0, 0.0, 8192
    ).setTracked(false);

    public static final EntityAttribute MAX_TEMPERATURE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.max_temperature", 40.0, 0.0, 8192
    ).setTracked(false);

    public static final EntityAttribute FROST_RESISTANCE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.frost_resistance", 0.0, -10.0, 10.0
    ).setTracked(false);

    public static final EntityAttribute HEAT_RESISTANCE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.heat_resistance", 0.0, -10.0, 10.0
    ).setTracked(false);

}
