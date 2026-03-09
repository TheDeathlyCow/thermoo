package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public final class AttributeDodgeReduction implements TemperatureReduction {
    private final Holder<Attribute> coldResistanceAttribute;
    private final Holder<Attribute> heatResistanceAttribute;
    private final double scale;

    public AttributeDodgeReduction(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute, double scale) {
        this.coldResistanceAttribute = coldResistanceAttribute;
        this.heatResistanceAttribute = heatResistanceAttribute;
        this.scale = scale;
    }

    @Override
    public int applyReduction(LivingEntity target, int temperatureChange) {
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

    @Override
    public MapCodec<? extends TemperatureReduction> codec() {
        return null;
    }
}