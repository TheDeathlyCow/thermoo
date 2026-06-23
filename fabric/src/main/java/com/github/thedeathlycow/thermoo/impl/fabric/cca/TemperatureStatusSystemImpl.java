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

package com.github.thedeathlycow.thermoo.impl.fabric.cca;

import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSystem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

public class TemperatureStatusSystemImpl implements CardinalComponent, ServerTickingComponent {
    private final LivingEntity provider;

    public TemperatureStatusSystemImpl(LivingEntity provider) {
        this.provider = provider;
    }

    public void serverTick() {
        TemperatureStatusSystem.doTick(this.provider);
    }

    @Override
    public void readData(ValueInput readView) {
        // nothing here
    }

    @Override
    public void writeData(ValueOutput writeView) {
        // nothing here
    }
}