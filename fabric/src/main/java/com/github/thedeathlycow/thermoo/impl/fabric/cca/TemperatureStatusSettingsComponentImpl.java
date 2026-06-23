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

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusSelector;
import com.github.thedeathlycow.thermoo.impl.ecs.Settings;
import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSettingsComponent;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

import java.util.IdentityHashMap;
import java.util.Map;

public class TemperatureStatusSettingsComponentImpl implements TemperatureStatusSettingsComponent, CardinalComponent {
    private final Map<ResourceKey<TemperatureStatus>, Settings> value = new IdentityHashMap<>();
    private final LivingEntity provider;

    public TemperatureStatusSettingsComponentImpl(LivingEntity provider) {
        this.provider = provider;
    }

    @Override
    public Map<ResourceKey<TemperatureStatus>, Settings> getSettings() {
        return this.value;
    }

    @Override
    public LivingEntity getProvider() {
        return this.provider;
    }

    @Override
    public @Nullable Settings getSettings(Holder.Reference<TemperatureStatus> statusRef) {
        TemperatureStatusSelector selector = statusRef.value().selector();

        if (selector.appliesToEntityType(this.provider.typeHolder())) {
            return this.getSettingsChecked(statusRef);
        } else {
            return null;
        }
    }

    @Override
    public void readData(ValueInput readView) {
        this.value.clear();
        readView.read(Settings.SETTINGS_KEY, Settings.MAP_CODEC).ifPresent(this.value::putAll);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.store(Settings.SETTINGS_KEY, Settings.MAP_CODEC, this.value);
    }

    private Settings getSettingsChecked(Holder.Reference<TemperatureStatus> statusRef) {
        return this.value.computeIfAbsent(
                statusRef.key(),
                _ -> new Settings(statusRef.value().enabledByDefault())
        );
    }
}