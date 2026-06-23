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

package com.github.thedeathlycow.thermoo.api.entity.v1;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.attribute.AttributeData;
import dev.yumi.commons.event.Event;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.jetbrains.annotations.ApiStatus;

/**
 * Custom {@link Attribute}s provided by Thermoo
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
    public static final Holder<Attribute> MIN_TEMPERATURE = register(
            "min_temperature",
            new RangedAttribute(
                    "attribute.thermoo.min_temperature", 0.0, 0.0, 8192
            ).setSyncable(true)
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
    public static final Holder<Attribute> MAX_TEMPERATURE = register(
            "max_temperature",
            new RangedAttribute(
                    "attribute.thermoo.max_temperature", 0.0, 0.0, 8192
            ).setSyncable(true)
    );

    /**
     * A multiplier of the base max soaking ticks value of 600. By default, this is 1.
     * <p>
     * The final max soaking tick value for living entities is floor(600 * multiplier).
     */
    public static final Holder<Attribute> MAX_SOAKING_TICK_MULTIPLIER = register(
            "max_soaking_tick_multiplier",
            new RangedAttribute(
                    "attribute.thermoo.max_soaking_tick_multiplier", 1.0, 0.0, 8192
            ).setSyncable(true)
    );

    /**
     * The cold resistance of an entity. 1 point of frost resistance corresponds to a 10% cold reduction
     *
     * @see #HEAT_RESISTANCE
     */
    public static final Holder<Attribute> FROST_RESISTANCE = register(
            "frost_resistance",
            new RangedAttribute(
                    "attribute.thermoo.frost_resistance", 0.0, -10.0, 10.0
            ).setSyncable(true)
    );

    /**
     * The heat resistance of an entity. 1 point of heat resistance corresponds to a 10% heat reduction
     *
     * @see #FROST_RESISTANCE
     */
    public static final Holder<Attribute> HEAT_RESISTANCE = register(
            "heat_resistance",
            new RangedAttribute(
                    "attribute.thermoo.heat_resistance", 0.0, -10.0, 10.0
            ).setSyncable(true)
    );

    /**
     * The environment heat resistance of an entity. Environment heat resistance does not reduce the amount of heat
     * during a temperature change, but instead provides a chance to "dodge" the change all together. It is used ONLY
     * for {@link com.github.thedeathlycow.thermoo.api.environment.v2.event.ServerPlayerEnvironmentTickEvents environment temperature changes}.
     *
     * @see #ENVIRONMENT_FROST_RESISTANCE
     */
    public static final Holder<Attribute> ENVIRONMENT_HEAT_RESISTANCE = register(
            "environment_heat_resistance",
            new RangedAttribute(
                    "attribute.thermoo.environment_heat_resistance", 0.0, -1.0, 1.0
            ).setSyncable(true)
    );

    /**
     * The environment frost resistance of an entity. Environment frost resistance does not reduce the amount of cold
     * during a temperature change, but instead provides a chance to "dodge" the change all together. It is used ONLY
     * for {@link com.github.thedeathlycow.thermoo.api.environment.v2.event.ServerPlayerEnvironmentTickEvents environment temperature changes}.
     *
     * @see #ENVIRONMENT_HEAT_RESISTANCE
     */
    public static final Holder<Attribute> ENVIRONMENT_FROST_RESISTANCE = register(
            "environment_frost_resistance",
            new RangedAttribute(
                    "attribute.thermoo.environment_frost_resistance", 0.0, -1.0, 1.0
            ).setSyncable(true)
    );

    /**
     * Gets the base value event that corresponds to the attribute given. If the given attribute is not a Thermoo
     * attribute defined in this class, an {@link IllegalArgumentException} will be thrown.
     *
     * @param attribute The attribute to get the event for
     * @return Returns the event for the attribute
     * @throws IllegalArgumentException if the given attribute is not a thermoo attribute defined by this class
     */
    public static Event<Identifier, SetBaseAttributeValue> baseValueEvent(Holder<Attribute> attribute) {
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
         * to the entity as a temporary attribute modifier using the {@link net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation#ADD_VALUE}
         * operation.
         * <p>
         * Note: the actual base value will be unaffected. To modify the base value, use {@link net.minecraft.world.entity.ai.attributes.DefaultAttributes}.
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

    private static Holder<Attribute> register(String name, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Thermoo.id(name), attribute);
    }

    private ThermooAttributes() {

    }
}
