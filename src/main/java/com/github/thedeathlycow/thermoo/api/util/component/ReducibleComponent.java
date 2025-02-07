package com.github.thedeathlycow.thermoo.api.util.component;

import org.jetbrains.annotations.Contract;

/**
 * A component that allows for other components to be reduced with this one in a {@link ReducibleComponentMapBuilder}
 *
 * @param <T> The self type of this component (NOT the values that this component stores - the component itself)
 * @see com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent an example of how to
 * implement this interface
 */
public interface ReducibleComponent<T extends ReducibleComponent<T>> {
    /**
     * Merges another component into this one, and returns a new component
     *
     * @param other The other component to merge into this one
     * @return Returns a new instance of this component
     */
    @Contract("_->new")
    T reduceWith(T other);
}