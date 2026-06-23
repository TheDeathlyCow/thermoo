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

package com.github.thedeathlycow.thermoo.impl.environment.attribute;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record DoubleWithAlpha(double value, double alpha) {
    private static final Codec<DoubleWithAlpha> FULL_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.DOUBLE
                                    .fieldOf("value")
                                    .forGetter(DoubleWithAlpha::value),
                            Codec.doubleRange(0.0, 1.0)
                                    .optionalFieldOf("alpha", 1.0)
                                    .forGetter(DoubleWithAlpha::alpha)
                    )
                    .apply(instance, DoubleWithAlpha::new)
    );
    public static final Codec<DoubleWithAlpha> CODEC = Codec.either(Codec.DOUBLE, FULL_CODEC)
            .xmap(
                    either -> either.map(DoubleWithAlpha::new, doubleWithAlpha -> doubleWithAlpha),
                    doubleWithAlpha -> doubleWithAlpha.alpha() == 1.0
                            ? Either.left(doubleWithAlpha.value())
                            : Either.right(doubleWithAlpha)
            );

    public DoubleWithAlpha(double value) {
        this(value, 1.0f);
    }
}