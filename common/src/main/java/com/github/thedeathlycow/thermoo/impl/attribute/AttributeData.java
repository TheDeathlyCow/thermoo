/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.impl.attribute;

import com.github.thedeathlycow.thermoo.api.entity.v1.ThermooAttributes;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.commons.event.Event;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;

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

    private final Identifier location;
    private final Holder<Attribute> attribute;
    private final Event<Identifier, ThermooAttributes.SetBaseAttributeValue> baseAttributeValueEvent;

    AttributeData(
            Identifier location,
            Holder<Attribute> attribute
    ) {
        this.location = location;
        this.attribute = attribute;
        this.baseAttributeValueEvent = createEvent(attribute);
    }

    public Identifier location() {
        return location;
    }

    public Holder<Attribute> attribute() {
        return attribute;
    }

    public Event<Identifier, ThermooAttributes.SetBaseAttributeValue> baseValueEvent() {
        return baseAttributeValueEvent;
    }

    private static Event<Identifier, ThermooAttributes.SetBaseAttributeValue> createEvent(Holder<Attribute> attribute) {
        return Thermoo.EVENT_MANAGER.create(
                ThermooAttributes.SetBaseAttributeValue.class,
                listeners -> (entity, baseValue) -> {
                    double value = 0.0;

                    for (ThermooAttributes.SetBaseAttributeValue listener : listeners) {
                        value = listener.getBaseValue(entity, value);
                    }

                    return value;
                }
        );
    }
}
