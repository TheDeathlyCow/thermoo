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

package com.github.thedeathlycow.thermoo.impl.fabric.platform;

import com.github.thedeathlycow.thermoo.impl.ecs.SyncedIntEntityComponent;
import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSettingsComponent;
import com.github.thedeathlycow.thermoo.impl.fabric.registry.ThermooCardinalEntityComponents;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooComponents;
import net.minecraft.world.entity.LivingEntity;

public class ThermooComponentsImpl implements ThermooComponents {
    @Override
    public SyncedIntEntityComponent getTemperatureComponent(LivingEntity provider) {
        return ThermooCardinalEntityComponents.TEMPERATURE.get(provider);
    }

    @Override
    public SyncedIntEntityComponent getWetnessComponent(LivingEntity provider) {
        return ThermooCardinalEntityComponents.WETNESS.get(provider);
    }

    @Override
    public TemperatureStatusSettingsComponent getTemperatureStatusSettings(LivingEntity provider) {
        return ThermooCardinalEntityComponents.TEMPERATURE_STATUS_SETTINGS.get(provider);
    }

    @Override
    public void doSyncTemperatureComponent(LivingEntity provider) {
        ThermooCardinalEntityComponents.TEMPERATURE.sync(provider);
    }

    @Override
    public void doSyncWetnessComponent(LivingEntity provider) {
        ThermooCardinalEntityComponents.WETNESS.sync(provider);
    }
}