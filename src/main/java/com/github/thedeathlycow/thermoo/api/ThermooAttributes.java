package com.github.thedeathlycow.thermoo.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;

/**
 * Custom {@link EntityAttribute}s provided by Thermoo
 */
public final class ThermooAttributes {

    /**
     * The minimum temperature of an entity. By default, this is 0.
     * <p>
     * Note that this is separate from {@link #MAX_TEMPERATURE}. Each point of this attribute is decreases the minimum
     * temperature of an entity by 140 points (140 points is the maximum number of freezing ticks that entities may have
     * for powder snow freezing in vanilla).
     * <p>
     * To set its base value, use {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController#getBaseValueForAttribute(EntityAttribute, LivingEntity)}
     * and check that this attribute is the provided attribute argument.
     *
     * @see #MAX_TEMPERATURE
     */
    public static final EntityAttribute MIN_TEMPERATURE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.min_temperature", 0.0, 0.0, 8192
    ).setTracked(false);

    /**
     * The maximum temperature of an entity. By default, this is 0.
     * <p>
     * Note that this is separate from {@link #MIN_TEMPERATURE}. Each point of this attribute is increases the maximum
     * temperature of an entity by 140 points (140 points is the maximum number of freezing ticks that entities may have
     * for powder snow freezing in vanilla).
     * <p>
     * To set its base value, use {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController#getBaseValueForAttribute(EntityAttribute, LivingEntity)}
     * and check that this attribute is the provided attribute argument.
     *
     * @see #MIN_TEMPERATURE
     */
    public static final EntityAttribute MAX_TEMPERATURE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.max_temperature", 0.0, 0.0, 8192
    ).setTracked(false);

    /**
     * The cold resistance of an entity. 1 point of frost resistance corresponds to a 10% cold reduction
     * <p>
     * To set its base value, use {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController#getBaseValueForAttribute(EntityAttribute, LivingEntity)}
     * and check that this attribute is the provided attribute argument.
     *
     * @see #HEAT_RESISTANCE
     */
    public static final EntityAttribute FROST_RESISTANCE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.frost_resistance", 0.0, -10.0, 10.0
    ).setTracked(false);

    /**
     * The heat resistance of an entity. 1 point of heat resistance corresponds to a 10% heat reduction
     * <p>
     * To set its base value, use {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController#getBaseValueForAttribute(EntityAttribute, LivingEntity)}
     * and check that this attribute is the provided attribute argument.
     *
     * @see #FROST_RESISTANCE
     */
    public static final EntityAttribute HEAT_RESISTANCE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.heat_resistance", 0.0, -10.0, 10.0
    ).setTracked(false);

    private ThermooAttributes() {
        
    }
}
