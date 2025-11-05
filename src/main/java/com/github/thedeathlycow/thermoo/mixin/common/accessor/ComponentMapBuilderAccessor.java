package com.github.thedeathlycow.thermoo.mixin.common.accessor;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DataComponentMap.Builder.class)
public interface ComponentMapBuilderAccessor {
    @Accessor("map")
    Reference2ObjectMap<DataComponentType<?>, Object> thermoo$getComponents();
}