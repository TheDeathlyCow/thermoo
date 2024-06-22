package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

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
     * To set its base value, use {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController#getBaseValueForAttribute(RegistryEntry, LivingEntity)}
     * and check that this attribute is the provided attribute argument.
     *
     * @see #MAX_TEMPERATURE
     */
    public static final RegistryEntry<EntityAttribute> MIN_TEMPERATURE = register(
            "generic.min_temperature",
            new ClampedEntityAttribute(
                    "attribute.thermoo.generic.min_temperature", 0.0, 0.0, 8192
            ).setTracked(true)
    );

    /**
     * The maximum temperature of an entity. By default, this is 0.
     * <p>
     * Note that this is separate from {@link #MIN_TEMPERATURE}. Each point of this attribute is increases the maximum
     * temperature of an entity by 140 points (140 points is the maximum number of freezing ticks that entities may have
     * for powder snow freezing in vanilla).
     * <p>
     * To set its base value, use {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController#getBaseValueForAttribute(RegistryEntry, LivingEntity)}
     * and check that this attribute is the provided attribute argument.
     *
     * @see #MIN_TEMPERATURE
     */
    public static final RegistryEntry<EntityAttribute> MAX_TEMPERATURE = register(
            "generic.max_temperature",
            new ClampedEntityAttribute(
                    "attribute.thermoo.generic.max_temperature", 0.0, 0.0, 8192
            ).setTracked(true)
    );

    /**
     * The cold resistance of an entity. 1 point of frost resistance corresponds to a 10% cold reduction
     * <p>
     * To set its base value, use {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController#getBaseValueForAttribute(RegistryEntry, LivingEntity)}
     * and check that this attribute is the provided attribute argument.
     *
     * @see #HEAT_RESISTANCE
     */
    public static final RegistryEntry<EntityAttribute> FROST_RESISTANCE = register(
            "generic.frost_resistance",
            new ClampedEntityAttribute(
                    "attribute.thermoo.generic.frost_resistance", 0.0, -10.0, 10.0
            ).setTracked(true)
    );

    /**
     * The heat resistance of an entity. 1 point of heat resistance corresponds to a 10% heat reduction
     * <p>
     * To set its base value, use {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController#getBaseValueForAttribute(RegistryEntry, LivingEntity)}
     * and check that this attribute is the provided attribute argument.
     *
     * @see #FROST_RESISTANCE
     */
    public static final RegistryEntry<EntityAttribute> HEAT_RESISTANCE = register(
            "generic.heat_resistance",
            new ClampedEntityAttribute(
                    "attribute.thermoo.generic.heat_resistance", 0.0, -10.0, 10.0
            ).setTracked(true)
    );


    private static RegistryEntry<EntityAttribute> register(String name, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Thermoo.id(name), attribute);
    }

    private ThermooAttributes() {

    }
}
