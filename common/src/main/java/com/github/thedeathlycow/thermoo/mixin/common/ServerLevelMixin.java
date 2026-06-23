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

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.github.thedeathlycow.thermoo.impl.core.ThermooServerLevel;
import com.github.thedeathlycow.thermoo.impl.core.UpdateEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements ThermooServerLevel {
    @Unique
    private List<TemperatureChange> thermoo$tickingTemperatureSources;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(
            final MinecraftServer server,
            final Executor executor,
            final LevelStorageSource.LevelStorageAccess levelStorage,
            final ServerLevelData levelData,
            final ResourceKey<Level> dimension,
            final LevelStem levelStem,
            final boolean isDebug,
            final long biomeZoomSeed,
            final List<CustomSpawner> customSpawners,
            final boolean tickTime,
            CallbackInfo ci
    ) {
        this.thermoo$tickingTemperatureSources = server.registryAccess().lookupOrThrow(ThermooRegistries.TEMPERATURE_SOURCE)
                .listElements()
                .filter(ref -> ref.value().tickInterval() > 0 && UpdateEvents.hasRegisteredEvents(ref.key()))
                .map(TemperatureChange::create)
                .toList();
    }
    
    @Override
    public List<TemperatureChange> thermoo$tickingTemperatureSources() {
        return this.thermoo$tickingTemperatureSources;
    }
}