package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.environment.component.ReducibleComponent;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ComponentMap.Builder.class)
public class ComponentMapBuilderMixin {
    @Shadow
    @Final
    private Reference2ObjectMap<ComponentType<?>, Object> components;

    @WrapMethod(method = "put")
    private <T> void mergeReduciblesOnPut(
            ComponentType<T> type, @Nullable Object value, Operation<Void> original
    ) {
        if (value != null && this.components.get(type) instanceof ReducibleComponent<?> rBase) {
            value = forceMerge(rBase, (ReducibleComponent<?>) value);
        }

        original.call(type, value);
    }

    @SuppressWarnings("unchecked")
    private static <T> T forceMerge(ReducibleComponent<T> base, ReducibleComponent<?> other) {
        return base.mergeWith((T) other);
    }
}