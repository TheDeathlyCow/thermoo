package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public final class ScaledAttributeReduction implements TemperatureReduction {
    public static final MapCodec<ScaledAttributeReduction> CODEC = RecordCodecBuilder.mapCodec(
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
            ).apply(instance, ScaledAttributeReduction::new)
    );

    private final Holder<Attribute> coldResistanceAttribute;
    private final Holder<Attribute> heatResistanceAttribute;
    private final double scale;

    public ScaledAttributeReduction(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute, double scale) {
        this.coldResistanceAttribute = coldResistanceAttribute;
        this.heatResistanceAttribute = heatResistanceAttribute;
        this.scale = scale;
    }

    @Override
    public int applyReduction(LivingEntity target, int temperatureChange) {
        double resistance = temperatureChange < 0
                ? target.getAttributeValue(this.coldResistanceAttribute)
                : target.getAttributeValue(this.heatResistanceAttribute);

        double resistanceAsPercent = resistance * this.scale;

        return Mth.ceil((1 - resistanceAsPercent) * temperatureChange);
    }

    @Override
    public MapCodec<ScaledAttributeReduction> codec() {
        return CODEC;
    }

    public Holder<Attribute> coldResistanceAttribute() {
        return coldResistanceAttribute;
    }

    public Holder<Attribute> heatResistanceAttribute() {
        return heatResistanceAttribute;
    }

    public double scale() {
        return scale;
    }
}