package com.github.thedeathlycow.thermoo.api.environment.component;

import org.jetbrains.annotations.Contract;

public interface ReducibleComponent<T> {
    @Contract("_->new")
    T mergeWith(T other);
}