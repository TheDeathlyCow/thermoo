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

package com.github.thedeathlycow.thermoo.impl.test.platform;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.ecs.Settings;
import com.github.thedeathlycow.thermoo.impl.ecs.SyncedIntEntityComponent;
import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSettingsComponent;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooComponents;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ThermooComponentsImpl implements ThermooComponents {
    @Override
    public SyncedIntEntityComponent getTemperatureComponent(LivingEntity provider) {
        return new SyncedIntEntityComponent() {
            @Override
            public int getValue() {
                return 0;
            }

            @Override
            public void setValue(int value) {

            }

            @Override
            public boolean isDirty() {
                return false;
            }
        };
    }

    @Override
    public SyncedIntEntityComponent getWetnessComponent(LivingEntity provider) {
        return new SyncedIntEntityComponent() {
            @Override
            public int getValue() {
                return 0;
            }

            @Override
            public void setValue(int value) {

            }

            @Override
            public boolean isDirty() {
                return false;
            }
        };
    }

    @Override
    public TemperatureStatusSettingsComponent getTemperatureStatusSettings(LivingEntity provider) {
        return new TemperatureStatusSettingsComponent() {
            @Override
            public Map<ResourceKey<TemperatureStatus>, Settings> getSettings() {
                return Map.of();
            }

            @Override
            public LivingEntity getProvider() {
                return provider;
            }

            @Override
            public @Nullable Settings getSettings(Holder.Reference<TemperatureStatus> statusRef) {
                return null;
            }
        };
    }

    @Override
    public void doSyncTemperatureComponent(LivingEntity provider) {

    }

    @Override
    public void doSyncWetnessComponent(LivingEntity provider) {

    }
}