package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.mixin.common.ComponentMapBuilderAccessor;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import org.jetbrains.annotations.Contract;

public interface ReducibleComponent<S extends ReducibleComponent<S, V>, V> {

    V value();

    @Contract("_->new")
    S mergeWith(ReducibleComponent<S, V> other);

    static ComponentMap reduce(ComponentMap base, ComponentMap other) {
        ComponentMap.Builder builder = ComponentMap.builder();
        ComponentMapBuilderAccessor accessor = (ComponentMapBuilderAccessor) builder;

        builder.addAll(base);

        // yikes lol
        for (Component<?> component : other) {
            accessor.thermoo$invokePut(component.type(), component.value());

            Component<?> baseComponent = base.copy(component.type());

            if (baseComponent != null && baseComponent.value() instanceof ReducibleComponent<?, ?> rBase) {
                accessor.thermoo$invokePut(component.type(), forceMerge(rBase, (ReducibleComponent<?, ?>) component.value()));
            }
        }

        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private static <S extends ReducibleComponent<S, V>, V> ReducibleComponent<S, V> forceMerge(
            ReducibleComponent<S, V> base,
            ReducibleComponent<?, ?> other
    ) {
        return base.mergeWith((ReducibleComponent<S, V>) other);
    }
}