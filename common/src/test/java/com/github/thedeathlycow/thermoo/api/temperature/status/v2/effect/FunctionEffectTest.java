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

package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.ThermooTest;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapLike;
import net.minecraft.util.GsonHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FunctionEffectTest {

    @BeforeAll
    static void setup() {
        ThermooTest.bootstrapRegistries();
    }

    @ParameterizedTest
    @ValueSource(
            ints = {0, 1, 2, 3, 4}
    )
    void validPermissionLevel_decode_doesNotFail(int permissionLevel) {
        var json = createJson(JsonOps.INSTANCE, permissionLevel);
        DataResult<FunctionEffect> result = FunctionEffect.CODEC.decode(JsonOps.INSTANCE, json);

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(permissionLevel, result.getOrThrow().permissionLevel());
    }

    @ParameterizedTest
    @ValueSource(
            ints = {Integer.MIN_VALUE, -1, 5, Integer.MAX_VALUE}
    )
    void invalidPermissionLevel_decode_fails(int permissionLevel) {
        var json = createJson(JsonOps.INSTANCE, permissionLevel);
        DataResult<FunctionEffect> result = FunctionEffect.CODEC.decode(JsonOps.INSTANCE, json);

        Assertions.assertTrue(result.isError());
    }

    private static MapLike<JsonElement> createJson(DynamicOps<JsonElement> ops, int permissionLevel) {
        JsonElement json = GsonHelper.parse(
                String.format("""
                                {
                                    "function": "test:test",
                                    "permission_level": %d
                                }
                                """,
                        permissionLevel
                )
        );

        return JsonOps.INSTANCE.getMap(json).getOrThrow();
    }
}
