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

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureRecord;
import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import net.minecraft.world.attribute.modifier.AttributeModifier;

public interface TemperatureModifier<Argument> extends AttributeModifier<TemperatureRecord, Argument> {
    Simple ADD = TemperatureRecord::sum;
    Simple SUBTRACT = (a, b) -> a.sum(new TemperatureRecord(-b.value(), b.unit()));
    Simple MINIMUM = (a, b) -> a.compareTo(b) < 0 ? a : b;
    Simple MAXIMUM = (a, b) -> a.compareTo(b) < 0 ? b : a;

    static TemperatureRecord lerp(float delta, TemperatureRecord a, TemperatureRecord b) {
        double interpolated = Mth.lerp(delta, a.value(), b.valueInUnit(a.unit()));
        return new TemperatureRecord(interpolated, a.unit());
    }

    @FunctionalInterface
    interface Simple extends TemperatureModifier<TemperatureRecord> {
        @Override
        default Codec<TemperatureRecord> argumentCodec(EnvironmentAttribute<TemperatureRecord> attribute) {
            return TemperatureRecord.CODEC;
        }

        @Override
        default LerpFunction<TemperatureRecord> argumentKeyframeLerp(EnvironmentAttribute<TemperatureRecord> attribute) {
            return TemperatureModifier::lerp;
        }
    }
}