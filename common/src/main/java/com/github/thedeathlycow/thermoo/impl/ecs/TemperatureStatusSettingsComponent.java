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

package com.github.thedeathlycow.thermoo.impl.ecs;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureEffectContextImpl;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusImpl;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface TemperatureStatusSettingsComponent {
    Map<ResourceKey<TemperatureStatus>, Settings> getSettings();

    LivingEntity getProvider();

    @Nullable
    Settings getSettings(Holder.Reference<TemperatureStatus> statusRef);

    default boolean setEffectEnabled(Holder.Reference<TemperatureStatus> statusRef, boolean enabled) {
        Settings settings = this.getSettings(statusRef);

        if (settings != null && settings.enabled() != enabled) {
            settings.setEnabled(enabled);

            // this is meant to ensure that the effect is cleaned up right away and not have to wait for the next
            // interval check, especially if that interval is long.
            if (!settings.enabled() && settings.applied()) {
                ((TemperatureStatusImpl) statusRef.value()).remove(this.getProvider(), TemperatureEffectContextImpl.INSTANCE);
                settings.setApplied(false);
            }

            return true;
        }
        return false;
    }

    default boolean isEffectEnabled(Holder.Reference<TemperatureStatus> statusRef) {
        Settings settings = this.getSettings(statusRef);

        if (settings != null) {
            return settings.enabled();
        }

        return false;
    }
}