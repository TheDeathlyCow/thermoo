package com.github.thedeathlycow.thermoo.api.core.v2.source;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

/**
 * A temperature reduction that uses resistance attributes as a probability to either fully dodge or double an incoming
 * temperature change. Positive resistance values give a chance to dodge the change entirely, while negative resistance
 * values give a chance to double it. Resistance values are clamped to the range [-1, 1], where the absolute value is
 * used as the probability.
 */
public final class RandomlyDodgeReduction implements TemperatureReduction {
    public static final MapCodec<RandomlyDodgeReduction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BuiltInRegistries.ATTRIBUTE.holderByNameCodec()
                            .fieldOf("cold_resistance_attribute")
                            .forGetter(RandomlyDodgeReduction::coldResistanceAttribute),
                    BuiltInRegistries.ATTRIBUTE.holderByNameCodec()
                            .fieldOf("heat_resistance_attribute")
                            .forGetter(RandomlyDodgeReduction::heatResistanceAttribute)
            ).apply(instance, RandomlyDodgeReduction::new)
    );

    private final Holder<Attribute> coldResistanceAttribute;
    private final Holder<Attribute> heatResistanceAttribute;

    private RandomlyDodgeReduction(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute) {
        this.coldResistanceAttribute = coldResistanceAttribute;
        this.heatResistanceAttribute = heatResistanceAttribute;
    }

    /**
     * Creates a new randomly dodge reduction. This method is primarily intended for use with data generation.
     *
     * @param coldResistanceAttribute The attribute to use as the dodge probability against negative temperature
     *                                changes. May not be {@code null}, but may be the same as
     *                                {@code heatResistanceAttribute}.
     * @param heatResistanceAttribute The attribute to use as the dodge probability against positive temperature
     *                                changes. May not be {@code null}, but may be the same as
     *                                {@code coldResistanceAttribute}.
     * @return Returns a new reduction.
     */
    public static RandomlyDodgeReduction create(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute) {
        Preconditions.checkNotNull(coldResistanceAttribute);
        Preconditions.checkNotNull(heatResistanceAttribute);

        return new RandomlyDodgeReduction(coldResistanceAttribute, heatResistanceAttribute);
    }

    /**
     * Applies the reduction by using the relevant resistance attribute as a dodge probability. The resistance value
     * is clamped to [-1, 1] before use.
     * <ul>
     *   <li>If resistance is positive, there is a {@code resistance * 100%} chance the change is reduced to {@code 0}.
     *   <li>If resistance is negative, there is a {@code -resistance * 100%} chance the change is doubled.
     *   <li>If resistance is {@code 0}, the change is returned unmodified.
     * </ul>
     *
     * @param target            The target being affected by the temperature change.
     * @param context           The context of the temperature change.
     * @param temperatureChange The amount of the temperature change.
     * @return Returns {@code 0}, {@code temperatureChange}, or {@code 2 * temperatureChange} depending on the
     * result of the random roll.
     */
    @Override
    public int applyReduction(LivingEntity target, TemperatureChange context, int temperatureChange) {
        double resistance = temperatureChange < 0
                ? target.getAttributeValue(coldResistanceAttribute)
                : target.getAttributeValue(heatResistanceAttribute);

        resistance = Mth.clamp(resistance, -1, 1);

        if (resistance > 0) {
            return target.thermoo$getRandom().nextDouble() < resistance ? 0 : temperatureChange;
        } else if (resistance < 0) {
            return target.thermoo$getRandom().nextDouble() < -resistance ? 2 * temperatureChange : temperatureChange;
        } else {
            return temperatureChange;
        }
    }

    /**
     * @return Returns {@link #CODEC}
     */
    @Override
    public MapCodec<RandomlyDodgeReduction> codec() {
        return CODEC;
    }

    /**
     * The resistance attribute to be used as the dodge probability for negative temperature changes.
     */
    public Holder<Attribute> coldResistanceAttribute() {
        return coldResistanceAttribute;
    }

    /**
     * The resistance attribute to be used as the dodge probability for positive temperature changes.
     */
    public Holder<Attribute> heatResistanceAttribute() {
        return heatResistanceAttribute;
    }
}