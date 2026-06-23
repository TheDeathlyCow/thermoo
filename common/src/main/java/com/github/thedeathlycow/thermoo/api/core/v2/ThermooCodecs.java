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

package com.github.thedeathlycow.thermoo.api.core.v2;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

/**
 * Helpful codecs used by Thermoo. May be changed between MC versions as equivalent vanilla codecs are added.
 * <p>
 * Exposed in API for the convenience of API users.
 */
@ApiStatus.Experimental
public final class ThermooCodecs {

    /**
     * Creates a codec for an Enum. Either uses the enum ordinal or the name, but prefers the ordinal for more efficient
     * storage.
     *
     * @param clazz The class of the enum.
     * @param <E>   The enum type
     * @return Returns a codec for the enum class
     */
    public static <E extends Enum<E>> Codec<E> createEnumCodec(Class<E> clazz) {
        return Codec.either(
                Codec.INT.xmap(
                        ordinal -> clazz.getEnumConstants()[ordinal],
                        Enum::ordinal
                ),
                Codec.STRING.xmap(
                        name -> Enum.valueOf(clazz, name),
                        Enum::name
                )
        ).xmap(
                either -> either.left().orElseGet(() -> either.right().orElseThrow()),
                Either::left
        );
    }

    private ThermooCodecs() {

    }

}
