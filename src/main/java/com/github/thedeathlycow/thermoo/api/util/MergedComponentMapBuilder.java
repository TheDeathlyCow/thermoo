package com.github.thedeathlycow.thermoo.api.util;

import com.github.thedeathlycow.thermoo.api.environment.component.ReducibleComponent;
import com.github.thedeathlycow.thermoo.mixin.common.ComponentMapBuilderAccessor;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import org.jetbrains.annotations.Nullable;

public class MergedComponentMapBuilder {
    private final ComponentMap.Builder builder;

    public MergedComponentMapBuilder() {
        this.builder = ComponentMap.builder();
    }

    public <T> MergedComponentMapBuilder add(ComponentType<T> type, @Nullable T value) {
        this.put(type, value);
        return this;
    }

    public MergedComponentMapBuilder addAll(ComponentMap componentSet) {
        for (Component<?> component : componentSet) {
            this.put(component.type(), component.value());
        }

        return this;
    }

    private <T> void put(ComponentType<T> type, @Nullable Object value) {
        ComponentMapBuilderAccessor accesor = (ComponentMapBuilderAccessor) builder;
        if (value != null && accesor.thermoo$getComponents().get(type) instanceof ReducibleComponent<?> rBase) {
            value = forceMerge(rBase, (ReducibleComponent<?>) value);
        }
        accesor.thermoo$invokePut(type, value);
    }

    public ComponentMap build() {
        return this.builder.build();
    }

    @SuppressWarnings("unchecked")
    private static <T> T forceMerge(ReducibleComponent<T> base, ReducibleComponent<?> other) {
        return base.mergeWith((T) other);
    }
}