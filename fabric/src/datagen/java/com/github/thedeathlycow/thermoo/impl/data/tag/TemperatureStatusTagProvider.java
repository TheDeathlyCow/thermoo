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

package com.github.thedeathlycow.thermoo.impl.data.tag;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.tag.ConventionalTemperatureStatusTags;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.tag.TemperatureStatusTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class TemperatureStatusTagProvider extends FabricTagsProvider<TemperatureStatus> {
    public TemperatureStatusTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, ThermooRegistries.TEMPERATURE_STATUS, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        generateThermooTags();

        generateConventionalTags();

        generateTagAliases();
    }

    private void generateThermooTags() {
        builder(TemperatureStatusTags.APPLICATION_ORDER);

        builder(TemperatureStatusTags.HARMFUL);

        builder(TemperatureStatusTags.BENEFICIAL);

        builder(TemperatureStatusTags.NEUTRAL);

        builder(TemperatureStatusTags.COLD);

        builder(TemperatureStatusTags.WARM);
    }

    private void generateConventionalTags() {
        builder(ConventionalTemperatureStatusTags.HARMFUL);

        builder(ConventionalTemperatureStatusTags.BENEFICIAL);

        builder(ConventionalTemperatureStatusTags.NEUTRAL);

        builder(ConventionalTemperatureStatusTags.COLD);

        builder(ConventionalTemperatureStatusTags.WARM);
    }

    private void generateTagAliases() {
        aliasGroup("harmful").add(TemperatureStatusTags.HARMFUL).add(ConventionalTemperatureStatusTags.HARMFUL);

        aliasGroup("beneficial").add(TemperatureStatusTags.BENEFICIAL).add(ConventionalTemperatureStatusTags.BENEFICIAL);

        aliasGroup("neutral").add(TemperatureStatusTags.NEUTRAL).add(ConventionalTemperatureStatusTags.NEUTRAL);

        aliasGroup("cold").add(TemperatureStatusTags.COLD).add(ConventionalTemperatureStatusTags.COLD);

        aliasGroup("warm").add(TemperatureStatusTags.WARM).add(ConventionalTemperatureStatusTags.WARM);
    }
}