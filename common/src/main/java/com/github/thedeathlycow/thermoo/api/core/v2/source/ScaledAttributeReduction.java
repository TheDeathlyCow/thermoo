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

package com.github.thedeathlycow.thermoo.api.core.v2.source;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

/**
 * A temperature reduction that reduces an incoming temperature linearly based on a scale factor and resistance attribute.
 * Allows for different attributes to be used based on whether the change is freezing (cold resistance) or warming
 * (heat resistance).
 */
public sealed class ScaledAttributeReduction implements TemperatureReduction permits ReinforcingAttributeReduction {
    public static final MapCodec<ScaledAttributeReduction> CODEC = createCodec(ScaledAttributeReduction::new);

    private final Holder<Attribute> coldResistanceAttribute;
    private final Holder<Attribute> heatResistanceAttribute;
    private final double scale;

    ScaledAttributeReduction(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute, double scale) {
        this.coldResistanceAttribute = coldResistanceAttribute;
        this.heatResistanceAttribute = heatResistanceAttribute;
        this.scale = scale;
    }

    /**
     * Creates a new scaled attribute reduction. This method is primarily intended for use with data generation.
     *
     * @param coldResistanceAttribute The attribute to use for resistance against negative temperature changes. May not
     *                                be {@code null}, but may be the same as {@code heatResistanceAttribute}.
     * @param heatResistanceAttribute The attribute to use for resistance against positive temperature changes. May not
     *                                be {@code null}, but may be the same as {@code heatResistanceAttribute}.
     * @param scale                   The scale to modify the effectiveness of the resistance. Must be finite.
     * @return Returns a new reduction.
     */
    public static ScaledAttributeReduction create(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute, double scale) {
        Preconditions.checkNotNull(coldResistanceAttribute);
        Preconditions.checkNotNull(heatResistanceAttribute);
        Preconditions.checkArgument(Double.isFinite(scale), "Scale must be finite");

        return new ScaledAttributeReduction(coldResistanceAttribute, heatResistanceAttribute, scale);
    }

    /**
     * Creates a new scaled attribute reduction with a scale of 1. This method is primarily intended for use with data
     * generation.
     *
     * @param coldResistanceAttribute The attribute to use for resistance against negative temperature changes. May not
     *                                be {@code null}, but may be the same as {@code heatResistanceAttribute}.
     * @param heatResistanceAttribute The attribute to use for resistance against positive temperature changes. May not
     *                                be {@code null}, but may be the same as {@code heatResistanceAttribute}.
     * @return Returns a new reduction.
     */
    public static ScaledAttributeReduction create(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute) {
        return create(coldResistanceAttribute, heatResistanceAttribute, 1.0);
    }

    /**
     * Linearly reduces the temperature change based on the relevant resistance type.
     *
     * @param target            The target being affected by the temperature change.
     * @param context           The context of the temperature change.
     * @param temperatureChange The amount of the temperature change.
     * @return Returns a reduced temperature change. If the reduced temperature value is not an integer, returns the
     * {@link Math#ceil(double)} of the value.
     */
    @Override
    public int applyReduction(LivingEntity target, TemperatureChange context, int temperatureChange) {
        double resistance = temperatureChange < 0
                ? target.getAttributeValue(this.coldResistanceAttribute)
                : target.getAttributeValue(this.heatResistanceAttribute);

        double resistanceAsPercent = resistance * this.scale;

        return Mth.ceil((1 - resistanceAsPercent) * temperatureChange);
    }

    /**
     * @return Returns {@link #CODEC}.
     */
    @Override
    public MapCodec<? extends ScaledAttributeReduction> codec() {
        return CODEC;
    }

    /**
     * The resistance attribute to be used for negative temperature changes.
     */
    public final Holder<Attribute> coldResistanceAttribute() {
        return coldResistanceAttribute;
    }

    /**
     * The resistance attribute to be used for positive temperature changes.
     */
    public final Holder<Attribute> heatResistanceAttribute() {
        return heatResistanceAttribute;
    }

    /**
     * The scale modifies the effectiveness of the resistance attributes.
     *
     * @return Returns a finite double.
     */
    public final double scale() {
        return scale;
    }

    protected static <T extends ScaledAttributeReduction> MapCodec<T> createCodec(
            Function3<Holder<Attribute>, Holder<Attribute>, Double, T> constructor
    ) {
        return RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        BuiltInRegistries.ATTRIBUTE.holderByNameCodec()
                                .fieldOf("cold_resistance_attribute")
                                .forGetter(ScaledAttributeReduction::coldResistanceAttribute),
                        BuiltInRegistries.ATTRIBUTE.holderByNameCodec()
                                .fieldOf("heat_resistance_attribute")
                                .forGetter(ScaledAttributeReduction::heatResistanceAttribute),
                        Codec.DOUBLE
                                .optionalFieldOf("scale", 1.0)
                                .forGetter(ScaledAttributeReduction::scale)
                ).apply(instance, constructor)
        );
    }
}