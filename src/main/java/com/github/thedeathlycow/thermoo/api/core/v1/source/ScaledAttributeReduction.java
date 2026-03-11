package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
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

    public static ScaledAttributeReduction create(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute, double scale) {
        Preconditions.checkNotNull(coldResistanceAttribute);
        Preconditions.checkNotNull(heatResistanceAttribute);
        Preconditions.checkArgument(Double.isFinite(scale), "Scale must be finite");

        return new ScaledAttributeReduction(coldResistanceAttribute, heatResistanceAttribute, scale);
    }

    public static ScaledAttributeReduction create(Holder<Attribute> coldResistanceAttribute, Holder<Attribute> heatResistanceAttribute) {
        return create(coldResistanceAttribute, heatResistanceAttribute, 1.0);
    }

    @Override
    public int applyReduction(LivingEntity target, int temperatureChange, TemperatureChange context) {
        double resistance = temperatureChange < 0
                ? target.getAttributeValue(this.coldResistanceAttribute)
                : target.getAttributeValue(this.heatResistanceAttribute);

        double resistanceAsPercent = resistance * this.scale;

        return Mth.ceil((1 - resistanceAsPercent) * temperatureChange);
    }

    @Override
    public MapCodec<? extends ScaledAttributeReduction> codec() {
        return CODEC;
    }

    public final Holder<Attribute> coldResistanceAttribute() {
        return coldResistanceAttribute;
    }

    public final Holder<Attribute> heatResistanceAttribute() {
        return heatResistanceAttribute;
    }

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