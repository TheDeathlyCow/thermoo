package com.github.thedeathlycow.thermoo.impl.attribute;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * Metadata for Thermoo attributes
 */
public enum AttributeData {

    MIN_TEMPERATURE(
            Thermoo.id("base_min_temperature"),
            ThermooAttributes.MIN_TEMPERATURE
    ),
    MAX_TEMPERATURE(
            Thermoo.id("base_max_temperature"),
            ThermooAttributes.MAX_TEMPERATURE
    ),
    MAX_SOAKING_TICK_MULTIPLIER(
            Thermoo.id("base_max_soaking_tick_multiplier"),
            ThermooAttributes.MAX_SOAKING_TICK_MULTIPLIER
    ),
    FROST_RESISTANCE(
            Thermoo.id("base_frost_resistance"),
            ThermooAttributes.FROST_RESISTANCE
    ),
    HEAT_RESISTANCE(
            Thermoo.id("base_heat_resistance"),
            ThermooAttributes.HEAT_RESISTANCE
    ),
    ENVIRONMENT_HEAT_RESISTANCE(
            Thermoo.id("base_environment_heat_resistance"),
            ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE
    ),
    ENVIRONMENT_FROST_RESISTANCE(
            Thermoo.id("base_environment_frost_resistance"),
            ThermooAttributes.ENVIRONMENT_FROST_RESISTANCE
    );

    private final Identifier id;
    private final RegistryEntry<EntityAttribute> attribute;
    private final Event<ThermooAttributes.SetBaseAttributeValue> baseAttributeValueEvent;

    AttributeData(
            Identifier id,
            RegistryEntry<EntityAttribute> attribute
    ) {
        this.id = id;
        this.attribute = attribute;
        this.baseAttributeValueEvent = createEvent(attribute);
    }

    public Identifier id() {
        return id;
    }

    public RegistryEntry<EntityAttribute> attribute() {
        return attribute;
    }

    public Event<ThermooAttributes.SetBaseAttributeValue> baseValueEvent() {
        return baseAttributeValueEvent;
    }

    private static Event<ThermooAttributes.SetBaseAttributeValue> createEvent(RegistryEntry<EntityAttribute> attribute) {
        return EventFactory.createArrayBacked(
                ThermooAttributes.SetBaseAttributeValue.class,
                listeners -> (entity, baseValue) -> {
                    double value = EnvironmentManager.INSTANCE.getController().getBaseValueForAttribute(attribute, entity);

                    for (ThermooAttributes.SetBaseAttributeValue listener : listeners) {
                        value = listener.getBaseValue(entity, value);
                    }

                    return value;
                }
        );
    }
}
