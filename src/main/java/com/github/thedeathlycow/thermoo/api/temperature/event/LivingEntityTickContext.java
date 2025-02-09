package com.github.thedeathlycow.thermoo.api.temperature.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public interface LivingEntityTickContext {
    @NotNull
    LivingEntity affected();

    @NotNull
    ServerWorld world();

    @NotNull
    BlockPos pos();
}