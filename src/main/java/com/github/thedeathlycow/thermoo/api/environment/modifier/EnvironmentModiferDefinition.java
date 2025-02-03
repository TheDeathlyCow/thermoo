package com.github.thedeathlycow.thermoo.api.environment.modifier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class EnvironmentModiferDefinition<T> {
    private final ValueSource<T> value;

    private final CombinationFunction<T> function;

    public EnvironmentModiferDefinition(ValueSource<T> value, CombinationFunction<T> function) {
        this.value = value;
        this.function = function;
    }

    public T apply(World world, BlockPos pos, T runningValue) {
        T shift = this.value.getValue(world, pos);
        return this.function.combine(runningValue, shift);
    }
}