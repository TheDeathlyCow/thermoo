package com.github.thedeathlycow.thermoo.impl.environment.attribute;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import net.minecraft.world.attribute.modifier.AttributeModifier;

public interface DoubleModifier<Argument> extends AttributeModifier<Double, Argument> {
    DoubleModifier<DoubleWithAlpha> ALPHA_BLEND = new DoubleModifier<>() {
        public Double apply(Double value, DoubleWithAlpha withAlpha) {
            return Mth.lerp(withAlpha.alpha(), value, withAlpha.value());
        }

        @Override
        public Codec<DoubleWithAlpha> argumentCodec(EnvironmentAttribute<Double> environmentAttribute) {
            return DoubleWithAlpha.CODEC;
        }

        @Override
        public LerpFunction<DoubleWithAlpha> argumentKeyframeLerp(EnvironmentAttribute<Double> environmentAttribute) {
            return (value, alpha1, alpha2) -> new DoubleWithAlpha(
                    Mth.lerp(value, alpha1.value(), alpha2.value()),
                    Mth.lerp(value, alpha1.alpha(), alpha2.alpha())
            );
        }
    };

    DoubleModifier.Simple ADD = Double::sum;
    DoubleModifier.Simple SUBTRACT = (a, b) -> a - b;
    DoubleModifier.Simple MULTIPLY = (a, b) -> a * b;
    DoubleModifier.Simple MINIMUM = Math::min;
    DoubleModifier.Simple MAXIMUM = Math::max;

    @FunctionalInterface
    interface Simple extends DoubleModifier<Double> {
        @Override
        default Codec<Double> argumentCodec(EnvironmentAttribute<Double> environmentAttribute) {
            return Codec.DOUBLE;
        }

        @Override
        default LerpFunction<Double> argumentKeyframeLerp(EnvironmentAttribute<Double> environmentAttribute) {
            return Mth::lerp;
        }
    }
}