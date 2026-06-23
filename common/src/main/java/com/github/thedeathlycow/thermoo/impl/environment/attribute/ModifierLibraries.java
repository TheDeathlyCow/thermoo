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
import net.minecraft.world.attribute.modifier.AttributeModifier;

import java.util.Map;

public final class ModifierLibraries {
    public static final Map<AttributeModifier.OperationId, AttributeModifier<Double, ?>> DOUBLE = Map.of(
            AttributeModifier.OperationId.ALPHA_BLEND,
            DoubleModifier.ALPHA_BLEND,
            AttributeModifier.OperationId.ADD,
            DoubleModifier.ADD,
            AttributeModifier.OperationId.SUBTRACT,
            DoubleModifier.SUBTRACT,
            AttributeModifier.OperationId.MULTIPLY,
            DoubleModifier.MULTIPLY,
            AttributeModifier.OperationId.MINIMUM,
            DoubleModifier.MINIMUM,
            AttributeModifier.OperationId.MAXIMUM,
            DoubleModifier.MAXIMUM
    );

    public static final Map<AttributeModifier.OperationId, AttributeModifier<TemperatureRecord, ?>> TEMPERATURE_RECORD = Map.of(
            AttributeModifier.OperationId.ADD,
            TemperatureModifier.ADD,
            AttributeModifier.OperationId.SUBTRACT,
            TemperatureModifier.SUBTRACT,
            AttributeModifier.OperationId.MINIMUM,
            TemperatureModifier.MINIMUM,
            AttributeModifier.OperationId.MAXIMUM,
            TemperatureModifier.MAXIMUM
    );

    private ModifierLibraries() {

    }
}