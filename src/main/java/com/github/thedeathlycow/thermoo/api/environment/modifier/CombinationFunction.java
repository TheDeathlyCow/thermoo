package com.github.thedeathlycow.thermoo.api.environment.modifier;

public interface CombinationFunction<T> {
    T combine(T runningValue, T shiftValue);
}
