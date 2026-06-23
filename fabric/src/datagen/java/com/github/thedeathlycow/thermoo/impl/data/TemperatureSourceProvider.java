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

package com.github.thedeathlycow.thermoo.impl.data;

import com.github.thedeathlycow.thermoo.api.entity.v1.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.core.v2.source.*;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class TemperatureSourceProvider extends FabricDynamicRegistryProvider {
    public TemperatureSourceProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.add(
                TemperatureSources.ABSOLUTE,
                TemperatureSource.builder(Component.translatable("thermoo.temperature_source.absolute"))
                        .build()
        );

        entries.add(
                TemperatureSources.ACTIVE,
                TemperatureSource.builder(Component.translatable("thermoo.temperature_source.active"))
                        .withReduction(ScaledAttributeReduction.create(
                                ThermooAttributes.FROST_RESISTANCE,
                                ThermooAttributes.HEAT_RESISTANCE,
                                0.1
                        ))
                        .withTickInterval(1)
                        .build()
        );

        entries.add(
                TemperatureSources.PASSIVE,
                TemperatureSource.builder(Component.translatable("thermoo.temperature_source.passive"))
                        .withReduction(ReinforcingAttributeReduction.create(
                                ThermooAttributes.FROST_RESISTANCE,
                                ThermooAttributes.HEAT_RESISTANCE,
                                0.1
                        ))
                        .withTickInterval(1)
                        .build()
        );

        entries.add(
                TemperatureSources.ENVIRONMENT,
                TemperatureSource.builder(Component.translatable("thermoo.temperature_source.environment"))
                        .withReduction(RandomlyDodgeReduction.create(
                                ThermooAttributes.FROST_RESISTANCE,
                                ThermooAttributes.HEAT_RESISTANCE
                        ))
                        .build()
        );
    }

    @Override
    public String getName() {
        return "ThermooTemperatureSources";
    }
}