package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

/**
 * A temperature reduction that only applies when the temperature change reinforces the target's current temperature
 * state. That is, cold resistance is only applied when the target is already cold and the change is negative, and
 * heat resistance is only applied when the target is already warm and the change is positive. When the change does
 * not reinforce the current state, the temperature change is passed through unmodified.
 *
 * @see ScaledAttributeReduction
 */
public final class ReinforcingAttributeReduction extends ScaledAttributeReduction {
    public static final MapCodec<ReinforcingAttributeReduction> CODEC = ScaledAttributeReduction.createCodec(ReinforcingAttributeReduction::new);

    private ReinforcingAttributeReduction(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute, double scale) {
        super(coldResistanceAttribute, heatResistanceAttribute, scale);
    }

    /**
     * Creates a new reinforcing attribute reduction. This method is primarily intended for use with data generation.
     *
     * @param coldResistanceAttribute The attribute to use for resistance against negative temperature changes. May not
     *                                be {@code null}, but may be the same as {@code heatResistanceAttribute}.
     * @param heatResistanceAttribute The attribute to use for resistance against positive temperature changes. May not
     *                                be {@code null}, but may be the same as {@code coldResistanceAttribute}.
     * @param scale                   The scale to modify the effectiveness of the resistance. Must be finite.
     * @return Returns a new reduction.
     */
    public static ReinforcingAttributeReduction create(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute, double scale) {
        Preconditions.checkNotNull(coldResistanceAttribute);
        Preconditions.checkNotNull(heatResistanceAttribute);
        Preconditions.checkArgument(Double.isFinite(scale), "Scale must be finite");

        return new ReinforcingAttributeReduction(coldResistanceAttribute, heatResistanceAttribute, scale);
    }

    /**
     * Creates a new reinforcing attribute reduction with a scale of 1. This method is primarily intended for use with
     * data generation.
     *
     * @param coldResistanceAttribute The attribute to use for resistance against negative temperature changes. May not
     *                                be {@code null}, but may be the same as {@code heatResistanceAttribute}.
     * @param heatResistanceAttribute The attribute to use for resistance against positive temperature changes. May not
     *                                be {@code null}, but may be the same as {@code coldResistanceAttribute}.
     * @return Returns a new reduction.
     */
    public static ReinforcingAttributeReduction create(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute) {
        return create(coldResistanceAttribute, heatResistanceAttribute, 1.0);
    }

    /**
     * Linearly reduces the temperature change based on the relevant resistance type, but only when the change
     * reinforces the target's current temperature state. If the change does not reinforce the current state,
     * the temperature change is returned unmodified.
     *
     * @param target            The target being affected by the temperature change.
     * @param context           The context of the temperature change.
     * @param temperatureChange The amount of the temperature change.
     * @return Returns the reduced temperature change if reinforcing, or the original temperature change otherwise.
     * If the reduced temperature value is not an integer, returns the {@link Math#ceil(double)} of the value.
     */
    @Override
    public int applyReduction(LivingEntity target, TemperatureChange context, int temperatureChange) {
        boolean isReinforcing = (target.thermoo$isCold() && temperatureChange < 0)
                || (target.thermoo$isWarm() && temperatureChange > 0);

        if (isReinforcing) {
            return super.applyReduction(target, context, temperatureChange);
        } else {
            return temperatureChange;
        }
    }

    /**
     * @return Returns {@link #CODEC}
     */
    @Override
    public MapCodec<ReinforcingAttributeReduction> codec() {
        return CODEC;
    }
}