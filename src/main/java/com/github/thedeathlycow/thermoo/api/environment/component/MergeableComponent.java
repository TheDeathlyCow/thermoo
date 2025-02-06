package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.mixin.common.ComponentMapBuilderAccessor;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.List;

public interface MergeableComponent<T> {
    @Contract("_->new")
    T mergeWith(Collection<ComponentMap> modifiers);

    static ComponentMap merge(ComponentMap base, List<ComponentMap> maps) {
        ComponentMap.Builder builder = ComponentMap.builder();
        ComponentMapBuilderAccessor accessor = (ComponentMapBuilderAccessor) builder;

        for (Component<?> component : base) {
            if (component.value() instanceof MergeableComponent<?> mergeable) {
                accessor.thermoo$invokePut(component.type(), mergeable.mergeWith(maps));
            } else {
                accessor.thermoo$invokePut(component.type(), component.value());
            }
        }
        return builder.build();
    }
}