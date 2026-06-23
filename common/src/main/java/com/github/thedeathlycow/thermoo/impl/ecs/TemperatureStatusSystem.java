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

package com.github.thedeathlycow.thermoo.impl.ecs;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusEvents;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureEffectContextImpl;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusImpl;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusManager;
import dev.yumi.commons.TriState;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class TemperatureStatusSystem {
    public static void doTick(LivingEntity provider) {
        Level level = provider.level();
        HolderLookup<TemperatureStatus> statusLookup = level.holderLookup(ThermooRegistries.TEMPERATURE_STATUS);
        List<Holder.Reference<TemperatureStatus>> possibleStatuses = TemperatureStatusManager.getEffects(
                provider,
                statusLookup
        );
        TemperatureStatusSettingsComponent settingsComponent = ThermooServices.COMPONENTS.getTemperatureStatusSettings(provider);

        for (Holder.Reference<TemperatureStatus> statusRef : possibleStatuses) {
            if (provider.tickCount % statusRef.value().interval() != 0) {
                continue;
            }

            Settings settings = settingsComponent.getSettings(statusRef);
            updateStatus(provider, statusRef, settings);
        }
    }

    private static void updateStatus(LivingEntity provider, Holder.Reference<TemperatureStatus> statusRef, @Nullable Settings settings) {
        if (settings != null && settings.enabled()) {
            TriState allowed = TemperatureStatusEvents.ALLOW_TEMPERATURE_STATUS.invoker().allow(provider, statusRef);
            TemperatureStatusImpl status = (TemperatureStatusImpl) statusRef.value();

            boolean wasApplied = settings.applied();
            boolean applied = allowed != TriState.FALSE && status.apply(provider, TemperatureEffectContextImpl.INSTANCE);

            if (wasApplied && !applied) {
                status.remove(provider, TemperatureEffectContextImpl.INSTANCE);
            }

            settings.setApplied(applied);
        }
    }

    private TemperatureStatusSystem() {

    }
}