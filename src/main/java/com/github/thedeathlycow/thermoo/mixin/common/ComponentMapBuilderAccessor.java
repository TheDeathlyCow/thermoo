package com.github.thedeathlycow.thermoo.mixin.common;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ComponentMap.Builder.class)
public interface ComponentMapBuilderAccessor {
    @Invoker("put")
    <T> void thermoo$invokePut(ComponentType<T> type, @Nullable Object value);
}