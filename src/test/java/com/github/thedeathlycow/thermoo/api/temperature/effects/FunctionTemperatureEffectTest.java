package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.ThermooTest;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
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
        JsonElement json = createJson(permissionLevel);

        DataResult<Pair<FunctionTemperatureEffect.Config, JsonElement>> result = FunctionTemperatureEffect.CODEC.decode(
                JsonOps.INSTANCE,
                json
        );

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(permissionLevel, result.getOrThrow().getFirst().permissionLevel());
    }

    @ParameterizedTest
    @ValueSource(
            ints = {Integer.MIN_VALUE, -1, 5, Integer.MAX_VALUE}
    )
    void invalidPermissionLevel_decode_permissionLevelIsDefault(int permissionLevel) {
        JsonElement json = createJson(permissionLevel);

        DataResult<Pair<FunctionTemperatureEffect.Config, JsonElement>> result = FunctionTemperatureEffect.CODEC.decode(
                JsonOps.INSTANCE,
                json
        );

        Assertions.assertFalse(result.isError());
        Assertions.assertEquals(
                FunctionTemperatureEffect.DEFAULT_PERMISSION_LEVEL,
                result.getOrThrow().getFirst().permissionLevel()
        );
    }

    private static JsonElement createJson(int permissionLevel) {
        return GsonHelper.parse(
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
    }
}
