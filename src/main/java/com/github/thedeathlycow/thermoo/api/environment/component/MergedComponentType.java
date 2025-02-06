package com.github.thedeathlycow.thermoo.api.environment.component;

import java.util.Collection;

public interface MergedComponentType<T> {
    T value();
    T merge(Collection<T> values);
}