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

class FunctionTemperatureEffectTest {

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
    void invalidPermissionLevel_decode_permissionLevelIsDefault(int permissionLevel) {
        var json = createJson(JsonOps.INSTANCE, permissionLevel);
        DataResult<FunctionEffect> result = FunctionEffect.CODEC.decode(JsonOps.INSTANCE, json);

        Assertions.assertFalse(result.isError());
        Assertions.assertEquals(
                FunctionEffect.DEFAULT_PERMISSION_LEVEL,
                result.getOrThrow().permissionLevel()
        );
    }

    private static MapLike<JsonElement> createJson(DynamicOps<JsonElement> ops, int permissionLevel) {
        JsonElement json = GsonHelper.parse(
                String.format("""
                                {
                                    "function": "test:test",
                                    "interval": 20,
                                    "permission_level": %d
                                }
                                """,
                        permissionLevel
                )
        );

        return JsonOps.INSTANCE.getMap(json).getOrThrow();
    }
}
