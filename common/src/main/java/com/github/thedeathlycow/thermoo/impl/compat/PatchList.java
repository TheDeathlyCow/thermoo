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

package com.github.thedeathlycow.thermoo.impl.compat;

import com.github.thedeathlycow.thermoo.impl.platform.Loader;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.yumi.commons.function.YumiPredicates;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.YumiMods;

import java.util.ArrayList;
import java.util.List;

public record PatchList(
        List<PatchedVersion> patches
) {
    public static final Codec<PatchList> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    PatchedVersion.CODEC.listOf()
                            .fieldOf("patches")
                            .forGetter(PatchList::patches)
            ).apply(instance, PatchList::new)
    );

    public List<ModContainer> getPatchAvailableMods(YumiMods loader) {
        String gameVersion = loader.getMod("minecraft")
                .orElseThrow()
                .getVersionString();

        List<ModContainer> patchAvailableMods = new ArrayList<>();
        Loader currentLoader = ThermooServices.PLATFORM.getLoader();

        for (PatchedVersion patch : this.patches) {
            if (patch.loader().matches(currentLoader) && patch.minecraftVersions().contains(gameVersion)) {
                this.extendPatchAvailableMods(loader, patchAvailableMods, patch);
            }
        }

        return patchAvailableMods;
    }

    private void extendPatchAvailableMods(YumiMods loader, List<ModContainer> patchAvailableMods, PatchedVersion patch) {
        for (String modid : patch.mods()) {
            loader.getMod(modid).ifPresent(patchAvailableMods::add);
        }
    }
}
