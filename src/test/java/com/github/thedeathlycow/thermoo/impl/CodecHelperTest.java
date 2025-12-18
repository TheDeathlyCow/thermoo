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