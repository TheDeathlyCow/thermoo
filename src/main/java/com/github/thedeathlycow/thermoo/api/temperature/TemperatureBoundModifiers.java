package com.github.thedeathlycow.thermoo.api.temperature;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Singleton class to handle modifiers for temperature bounds for specific entity types. Lower and upper bound modifiers
 * are applied additively to {@link ThermooAttributes#MIN_TEMPERATURE} and {@link ThermooAttributes#MAX_TEMPERATURE},
 * respectively. Modifiers are applied immediately upon entity creation, and are persisted on the entity forever.
 */
public final class TemperatureBoundModifiers {

    /**
     * Lazily-instantiated singleton instance of this class
     */
    private static TemperatureBoundModifiers instance = null;

    /**
     * Lower bounds modifiers map
     */
    private final Map<EntityType<? extends LivingEntity>, Double> lowerBounds = new HashMap<>();
    /**
     * Upper bounds modifiers map
     */
    private final Map<EntityType<? extends LivingEntity>, Double> upperBounds = new HashMap<>();

    /**
     * @return Returns the singleton instance of this class
     */
    public static synchronized TemperatureBoundModifiers getInstance() {
        if (instance == null) {
            instance = new TemperatureBoundModifiers();
        }

        return instance;
    }

    /**
     * @param type
     * @return
     */
    public Optional<Double> getLowerBoundIncrease(EntityType<? extends LivingEntity> type) {
        return Optional.ofNullable(lowerBounds.get(type));
    }

    public Optional<Double> getUpperBoundIncrease(EntityType<? extends LivingEntity> type) {
        return Optional.ofNullable(upperBounds.get(type));
    }

    /**
     * Additively increases the lower and upper bounds of temperature for the given {@code type} by applying an
     * attribute modifier to both {@link ThermooAttributes#MIN_TEMPERATURE} and {@link ThermooAttributes#MAX_TEMPERATURE}
     * with the respective min and max values, whenever an entity of the {@code type} is constructed.
     *
     * @param type     The entity type to increase the min and max temperature for
     * @param minValue The value to apply for the attribute modifier to min temperature
     * @param maxValue The value to apply for the attribute modifier to max temperature
     */
    public void setBounds(EntityType<? extends LivingEntity> type, double minValue, double maxValue) {
        setValue(type, minValue, lowerBounds);
        setValue(type, maxValue, upperBounds);
    }

    /**
     * Additively increases the lower bound of temperature for the given {@code type} by applying an attribute modifier
     * to {@link ThermooAttributes#MIN_TEMPERATURE} whenever an entity of the {@code type} is constructed.
     *
     * @param type     The entity type to increase the min temperature for
     * @param minValue The value to apply for the attribute modifier
     */
    public void increaseLowerBoundForType(EntityType<? extends LivingEntity> type, double minValue) {
        setValue(type, minValue, lowerBounds);
    }

    /**
     * Additively increases the upper bound of temperature for the given {@code type} by applying an attribute modifier
     * to {@link ThermooAttributes#MAX_TEMPERATURE} whenever an entity of the {@code type} is constructed.
     *
     * @param type     The entity type to increase the max temperature for
     * @param maxValue The value to apply for the attribute modifier
     */
    public void increaseUpperBoundForType(EntityType<? extends LivingEntity> type, double maxValue) {
        setValue(type, maxValue, upperBounds);
    }

    private void setValue(
            EntityType<? extends LivingEntity> type,
            double value,
            Map<EntityType<? extends LivingEntity>, Double> map
    ) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive!");
        }

        if (map.containsKey(type)) {
            Thermoo.LOGGER.warn(
                    "Overwriting duplicate entity type registration for temperature bound: {}",
                    Registries.ENTITY_TYPE.getId(type)
            );
        }

        map.put(type, value);
    }

    private TemperatureBoundModifiers() {

    }

}
