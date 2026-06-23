/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
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