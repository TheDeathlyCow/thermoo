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

package com.github.thedeathlycow.thermoo.impl;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;

import java.util.Optional;

public class CodecHelper {
    public static <T> Codec<Optional<T>> optionalCodec(Codec<T> baseCodec) {
        return Codec.either(baseCodec, Codec.EMPTY.codec())
                .xmap(
                        either -> either.map(Optional::of, right -> Optional.empty()),
                        optional -> optional.isPresent()
                                ? Either.left(optional.orElseThrow())
                                : Either.right(Unit.INSTANCE)
                );
    }

    private CodecHelper() {

    }
}