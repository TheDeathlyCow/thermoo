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

import com.github.thedeathlycow.thermoo.api.core.v2.ThermooLevel;
import com.github.thedeathlycow.thermoo.api.core.v2.source.BuiltinTemperatureSources;
import com.github.thedeathlycow.thermoo.impl.core.BuiltinTemperatureSourcesImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelMixin implements ThermooLevel {
    @Unique
    private BuiltinTemperatureSources thermoo$temperatureSources;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(
            final WritableLevelData levelData,
            final ResourceKey<Level> dimension,
            final RegistryAccess registryAccess,
            final Holder<DimensionType> dimensionTypeRegistration,
            final boolean isClientSide,
            final boolean isDebug,
            final long biomeZoomSeed,
            final int maxChainedNeighborUpdates,
            CallbackInfo ci
    ) {
        this.thermoo$temperatureSources = new BuiltinTemperatureSourcesImpl(registryAccess);
    }

    @Override
    @Unique
    public BuiltinTemperatureSources thermoo$temperatureSources() {
        return this.thermoo$temperatureSources;
    }
}