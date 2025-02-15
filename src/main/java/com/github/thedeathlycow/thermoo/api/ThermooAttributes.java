package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.attribute.AttributeData;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentHeatingMode;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;

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
     *
     * @see #FROST_RESISTANCE
     */
    public static final RegistryEntry<EntityAttribute> HEAT_RESISTANCE = register(
            "generic.heat_resistance",
            new ClampedEntityAttribute(
                    "attribute.thermoo.generic.heat_resistance", 0.0, -10.0, 10.0
            ).setTracked(true)
    );

    /**
     * The environment heat resistance of an entity. Environment heat resistance does not reduce the amount of heat
     * during a temperature change, but instead provides a chance to "dodge" the change all together. It is used ONLY
     * for {@link com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents environment temperature changes}.
     *
     * @see #ENVIRONMENT_FROST_RESISTANCE
     */
    public static final RegistryEntry<EntityAttribute> ENVIRONMENT_HEAT_RESISTANCE = register(
            "generic.environment_heat_resistance",
            new ClampedEntityAttribute(
                    "attribute.thermoo.environment_heat_resistance", 0.0, 0.0, 1.0
            ).setTracked(true)
    );

    /**
     * The environment frost resistance of an entity. Environment frost resistance does not reduce the amount of cold
     * during a temperature change, but instead provides a chance to "dodge" the change all together. It is used ONLY
     * for {@link com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents environment temperature changes}.
     *
     * @see #ENVIRONMENT_HEAT_RESISTANCE
     */
    public static final RegistryEntry<EntityAttribute> ENVIRONMENT_FROST_RESISTANCE = register(
            "generic.environment_frost_resistance",
            new ClampedEntityAttribute(
                    "attribute.thermoo.environment_frost_resistance", 0.0, 0.0, 1.0
            ).setTracked(true)
    );

    /**
     * Gets the base value event that corresponds to the attribute given. If the given attribute is not a Thermoo
     * attribute defined in this class, an {@link IllegalArgumentException} will be thrown.
     *
     * @param attribute The attribute to get the event for
     * @return Returns the event for the attribute
     * @throws IllegalArgumentException if the given attribute is not a thermoo attribute defined by this class
     */
    public static Event<SetBaseAttributeValue> baseValueEvent(RegistryEntry<EntityAttribute> attribute) {
        for (AttributeData data : AttributeData.values()) {
            if (attribute == data.attribute()) {
                return data.baseValueEvent();
            }
        }

        throw new IllegalArgumentException("Attribute " + attribute + " is not a Thermoo attribute!");
    }

    @FunctionalInterface
    public interface SetBaseAttributeValue {
        /**
         * Gets a base value for the event. If the value returned by this event is non-zero, then it will be applied
         * to the entity as a temporary attribute modifier using the {@link net.minecraft.entity.attribute.EntityAttributeModifier.Operation#ADD_VALUE}
         * operation.
         * <p>
         * Note: the actual base value will be unaffected. To modify the base value, use {@link net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry}.
         * This method is simply meant to be more compatible.
         *
         * @param entity    The entity to apply the attribute value to.
         * @param baseValue The value given by other listeners. Starts from 0.
         * @return Returns the value this listener wants to apply to the modifier.
         */
        double getBaseValue(LivingEntity entity, double baseValue);

        /**
         * Shorthand used for invoking this event without needing to specify the base value parameter. Should not be
         * extended by listeners.
         *
         * @param entity The entity to apply the attribute value to.
         * @return Returns the value this listener wants to apply to the modifier.
         * @see #getBaseValue(LivingEntity, double)
         */
        @ApiStatus.NonExtendable
        default double getBaseValue(LivingEntity entity) {
            return getBaseValue(entity, 0);
        }
    }

    private static RegistryEntry<EntityAttribute> register(String name, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Thermoo.id(name), attribute);
    }

    private ThermooAttributes() {

    }
}
