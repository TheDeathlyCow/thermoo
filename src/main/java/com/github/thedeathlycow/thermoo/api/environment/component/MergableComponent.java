package com.github.thedeathlycow.thermoo.api.environment.component;

import net.minecraft.component.ComponentMap;
import org.jetbrains.annotations.Contract;

import java.util.Collection;

public interface MergableComponent<T> {
    @Contract("_->new")
    T mergeWith(Collection<ComponentMap> modifiers);
}