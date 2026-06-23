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

package com.github.thedeathlycow.thermoo.impl.compat;

import com.github.thedeathlycow.thermoo.impl.platform.Loader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record PatchedVersion(
        Loader loader,
        List<String> minecraftVersions,
        List<String> mods
) {
    public static final Codec<PatchedVersion> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Loader.CODEC
                            .fieldOf("loader")
                            .forGetter(PatchedVersion::loader),
                    Codec.STRING.listOf()
                            .fieldOf("minecraft_versions")
                            .forGetter(PatchedVersion::minecraftVersions),
                    Codec.STRING.listOf()
                            .fieldOf("mods")
                            .forGetter(PatchedVersion::mods)
            ).apply(instance, PatchedVersion::new)
    );
}