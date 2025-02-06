package com.github.thedeathlycow.thermoo.api.environment.component;

import org.jetbrains.annotations.Contract;

public interface ReducableComponent<T> {
    @Contract("_->new")
    T mergeWith(T other);
}