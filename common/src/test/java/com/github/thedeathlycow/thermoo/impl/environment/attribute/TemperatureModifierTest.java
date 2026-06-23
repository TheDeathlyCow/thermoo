/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
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
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemperatureModifierTest {
    @Test
    void sub10Kfrom30K() {
        var a = new TemperatureRecord(30, TemperatureUnit.KELVIN);
        var b = new TemperatureRecord(10, TemperatureUnit.KELVIN);

        var difference = TemperatureModifier.SUBTRACT.apply(a, b);

        assertEquals(new TemperatureRecord(20, TemperatureUnit.KELVIN), difference);
    }

    @Test
    void sub30Kfrom10K() {
        var a = new TemperatureRecord(30, TemperatureUnit.KELVIN);
        var b = new TemperatureRecord(10, TemperatureUnit.KELVIN);

        var difference = TemperatureModifier.SUBTRACT.apply(b, a);

        assertEquals(new TemperatureRecord(-20, TemperatureUnit.KELVIN), difference);
    }
}