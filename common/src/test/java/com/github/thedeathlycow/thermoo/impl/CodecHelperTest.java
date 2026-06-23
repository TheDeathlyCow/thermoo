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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CodecHelperTest {
    @Test
    void decodeEmptyObject_isEmptyOptional() {
        Codec<Optional<Integer>> codec = CodecHelper.optionalCodec(Codec.INT);
        var json = new JsonObject();

        var result = codec.decode(JsonOps.INSTANCE, json);

        assertFalse(result.isError());
        assertTrue(result.getOrThrow().getFirst().isEmpty());
    }

    @Test
    void decodeInteger_isInteger() {
        Codec<Optional<Integer>> codec = CodecHelper.optionalCodec(Codec.INT);
        var json = new JsonPrimitive(67);

        var result = codec.decode(JsonOps.INSTANCE, json);

        assertFalse(result.isError());
        assertFalse(result.getOrThrow().getFirst().isEmpty());
        assertEquals(67, result.getOrThrow().getFirst().orElseThrow());
    }

    @Test
    void decodeArray_isError() {
        Codec<Optional<Integer>> codec = CodecHelper.optionalCodec(Codec.INT);
        var json = new JsonArray();

        var result = codec.decode(JsonOps.INSTANCE, json);

        assertTrue(result.isError());
        assertThrows(IllegalStateException.class, result::getOrThrow);
    }
}