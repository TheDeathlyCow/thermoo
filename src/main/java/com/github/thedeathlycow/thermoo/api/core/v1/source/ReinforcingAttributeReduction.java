package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public final class ReinforcingAttributeReduction extends ScaledAttributeReduction {
    public static final MapCodec<ReinforcingAttributeReduction> CODEC = ScaledAttributeReduction.createCodec(ReinforcingAttributeReduction::new);

    private ReinforcingAttributeReduction(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute, double scale) {
        super(coldResistanceAttribute, heatResistanceAttribute, scale);
    }

    public static ReinforcingAttributeReduction create(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute, double scale) {
        Preconditions.checkNotNull(coldResistanceAttribute);
        Preconditions.checkNotNull(heatResistanceAttribute);
        Preconditions.checkArgument(Double.isFinite(scale), "Scale must be finite");

        return new ReinforcingAttributeReduction(coldResistanceAttribute, heatResistanceAttribute, scale);
    }

    public static ReinforcingAttributeReduction create(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute) {
        return create(coldResistanceAttribute, heatResistanceAttribute, 1.0);
    }

    @Override
    public MapCodec<ReinforcingAttributeReduction> codec() {
        return CODEC;
    }

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
}