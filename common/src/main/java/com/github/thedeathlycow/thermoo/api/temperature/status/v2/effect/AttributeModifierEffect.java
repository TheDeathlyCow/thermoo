/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
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

package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectContext;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * A temperature effect that applies an attribute modifier to a victim.
 * <p>
 * Includes options to increase the modifier in strength with respect to the target's
 * {@linkplain TemperatureAware#thermoo$getTemperatureScale() current temperature scale}.
 */
public final class AttributeModifierEffect implements TemperatureEffect {
    public static final MapCodec<AttributeModifierEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BuiltInRegistries.ATTRIBUTE.holderByNameCodec()
                            .fieldOf("attribute_type")
                            .forGetter(AttributeModifierEffect::attribute),
                    Codec.DOUBLE
                            .fieldOf("value")
                            .forGetter(AttributeModifierEffect::value),
                    Identifier.CODEC
                            .fieldOf("id")
                            .forGetter(AttributeModifierEffect::id),
                    AttributeModifier.Operation.CODEC
                            .fieldOf("operation")
                            .forGetter(AttributeModifierEffect::operation),
                    Codec.BOOL
                            .optionalFieldOf("scale_with_temperature", false)
                            .forGetter(AttributeModifierEffect::scaleWithTemperature)
            ).apply(instance, AttributeModifierEffect::new)
    );

    private final Holder<Attribute> attribute;
    private final double value;
    private final Identifier id;
    private final AttributeModifier.Operation operation;
    private final boolean scaleWithTemperature;

    private AttributeModifierEffect(
            Holder<Attribute> attribute,
            double value,
            Identifier id,
            AttributeModifier.Operation operation,
            boolean scaleWithTemperature
    ) {
        this.value = value;
        this.attribute = attribute;
        this.id = id;
        this.operation = operation;
        this.scaleWithTemperature = scaleWithTemperature;
    }

    private static AttributeModifierEffect createChecked(
            Holder<Attribute> attribute,
            double value,
            Identifier id,
            AttributeModifier.Operation operation,
            boolean scaleWithTemperature
    ) {
        Preconditions.checkNotNull(attribute, "Attribute may not be null");
        Preconditions.checkArgument(Double.isFinite(value), "Value must be finite");
        Preconditions.checkNotNull(id, "ID may not be null");
        Preconditions.checkNotNull(operation, "Operation may not be null");

        return new AttributeModifierEffect(attribute, value, id, operation, scaleWithTemperature);
    }

    /**
     * Creates a new modifier effect for data generation. The returned effect is unscaled; the value it applies is
     * fixed.
     *
     * @throws NullPointerException     if any of {@code attribute}, {@code id}, or {@code operation} are {@code null}
     * @throws IllegalArgumentException if {@code value} is infinite or NaN
     */
    public static AttributeModifierEffect create(
            Holder<Attribute> attribute,
            double value,
            Identifier id,
            AttributeModifier.Operation operation
    ) {
        return createChecked(attribute, value, id, operation, false);
    }

    /**
     * Uses an existing attribute modifier to create a new effect for data generation.
     *
     * @throws NullPointerException     if any of {@code attribute}, {@code modifier}, or any of the {@code modifier}'s
     *                                  fields are {@code null}
     * @throws IllegalArgumentException if the {@code modifier}'s amount is infinite or NaN
     */
    public static AttributeModifierEffect create(Holder<Attribute> attribute, AttributeModifier modifier) {
        return create(attribute, modifier.amount(), modifier.id(), modifier.operation());
    }

    /**
     * Creates a new modifier effect for data generation. The returned effect will scale its value with a target's
     * temperature scale.
     *
     * @throws NullPointerException     if any of {@code attribute}, {@code id}, or {@code operation} are {@code null}
     * @throws IllegalArgumentException if {@code value} is infinite or NaN
     */
    public static AttributeModifierEffect createScaled(
            Holder<Attribute> attribute,
            double value,
            Identifier id,
            AttributeModifier.Operation operation
    ) {
        return createChecked(attribute, value, id, operation, true);
    }

    /**
     * Uses an existing attribute modifier to create a new effect for data generation. The returned effect will scale
     * its value with a target's temperature scale.
     */
    public static AttributeModifierEffect createScaled(Holder<Attribute> attribute, AttributeModifier modifier) {
        return createScaled(attribute, modifier.amount(), modifier.id(), modifier.operation());
    }

    /**
     * Applies an attribute modifier to the target.
     *
     * @param target The entity receiving the effect.
     * @param context Additional context for the effect.
     * @return Returns {@code true} if the attribute modifier should not be removed, {@code false} otherwise.
     */
    @Override
    public boolean apply(LivingEntity target, TemperatureEffectContext context) {
        AttributeInstance attrInstance = target.getAttribute(this.attribute);

        if (attrInstance != null) {
            if (this.scaleWithTemperature) {
                this.applyScaledAttribute(target, attrInstance);
            } else if (!attrInstance.hasModifier(this.id)) {
                attrInstance.addTransientModifier(new AttributeModifier(this.id, this.value, this.operation));
            }
            
            return true;
        }

        return false;
    }

    private void applyScaledAttribute(LivingEntity victim, AttributeInstance attrInstance) {
        AttributeModifier existingModifier = attrInstance.getModifier(this.id);
        double scaledValue = this.value * victim.thermoo$getTemperatureScale();

        if (existingModifier == null || existingModifier.amount() != scaledValue) {
            attrInstance.addOrUpdateTransientModifier(new AttributeModifier(this.id, scaledValue, this.operation));
        }
    }

    /**
     * Removes the attribute modifier from the target.
     *
     * @param target The entity the effect is being removed from.
     * @param context Additional context for the effect.
     */
    @Override
    public void remove(LivingEntity target, TemperatureEffectContext context) {
        AttributeInstance attributeInstance = target.getAttribute(this.attribute);

        if (attributeInstance != null) {
            attributeInstance.removeModifier(this.id);
        }
    }

    /**
     * @return Returns {@link #CODEC}.
     */
    @Override
    public MapCodec<AttributeModifierEffect> codec() {
        return CODEC;
    }

    /**
     * The value of the modifier. If this modifier is {@link #scaleWithTemperature() scaled} then this is the value at
     * +100% temperature scale.
     *
     * @return Returns a finite double
     */
    public double value() {
        return value;
    }

    /**
     * The attribute to be modified
     */
    public Holder<Attribute> attribute() {
        return attribute;
    }

    /**
     * The ID of the attribute modifier.
     */
    public Identifier id() {
        return id;
    }

    /**
     * The operation of the modifier.
     */
    public AttributeModifier.Operation operation() {
        return operation;
    }

    /**
     * Whether the modifier value applied by this effect is scaled with the target's temperature.
     */
    public boolean scaleWithTemperature() {
        return scaleWithTemperature;
    }
}