package com.github.thedeathlycow.thermoo.api.environment.modifier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ValueSource<T> {
    T getValue(World world, BlockPos pos);
}