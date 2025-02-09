package com.github.thedeathlycow.thermoo.mixin.common.accessor;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("getPosWithYOffset")
    BlockPos thermoo$invokeGetPosWithYOffset(float offset);
}