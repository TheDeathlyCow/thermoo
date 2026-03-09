package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

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

    public RandomlyDodgeReduction(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute) {
        this.coldResistanceAttribute = coldResistanceAttribute;
        this.heatResistanceAttribute = heatResistanceAttribute;
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
    public MapCodec<RandomlyDodgeReduction> codec() {
        return CODEC;
    }

    public Holder<Attribute> coldResistanceAttribute() {
        return coldResistanceAttribute;
    }

    public Holder<Attribute> heatResistanceAttribute() {
        return heatResistanceAttribute;
    }
}