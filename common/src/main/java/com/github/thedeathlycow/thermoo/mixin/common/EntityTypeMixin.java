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

package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureEffectCache;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(EntityType.class)
public class EntityTypeMixin implements TemperatureEffectCache {
    @Unique
    @Nullable
    private List<Holder.Reference<TemperatureStatus>> thermoo$effects = null;

    @Override
    @Unique
    public void thermoo$setStatuses(List<Holder.Reference<TemperatureStatus>> effects) {
        this.thermoo$effects = effects;
    }

    @Override
    @Unique
    public List<Holder.Reference<TemperatureStatus>> thermoo$getStatuses() {
        return this.thermoo$effects;
    }
}