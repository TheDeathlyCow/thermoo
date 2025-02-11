package com.github.thedeathlycow.thermoo.testmod.tests.environment.light;

import net.minecraft.test.PositionedException;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public final class LightTestHelper {
    public static void expectLightLevel(TestContext context, BlockPos pos, int expected) {
        BlockPos absolutePos = context.getAbsolutePos(pos);

        int lightLevel = context.getWorld().getLightLevel(absolutePos);

        if (expected != lightLevel) {
            throw new PositionedException(
                    "Expected " + expected + " light level",
                    absolutePos, pos,
                    context.getTick()
            );
        }
    }

    public static void expectLightLevel(TestContext context, BlockPos pos, LightType type, int expected) {
        BlockPos absolutePos = context.getAbsolutePos(pos);

        int lightLevel = context.getWorld().getLightLevel(type, absolutePos);

        if (expected != lightLevel) {
            throw new PositionedException(
                    "Expected " + expected + " " + type.name().toLowerCase() + " light level",
                    absolutePos, pos,
                    context.getTick()
            );
        }
    }

    private LightTestHelper() {

    }
}