package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import org.jetbrains.annotations.ApiStatus;

/**
 * Stores shared temperature change contexts for the {@linkplain TemperatureSources built in temperature sources}. All
 * sources stored by this class have no cause, direct cause, or position.
 */
@ApiStatus.NonExtendable
public interface BuiltinTemperatureSources {
    /**
     * @return Returns a shared temperature change context with a source of {@link TemperatureSources#ABSOLUTE}
     */
    TemperatureChange absolute();

    /**
     * @return Returns a shared temperature change context with a source of {@link TemperatureSources#ACTIVE}
     */
    TemperatureChange active();

    /**
     * @return Returns a shared temperature change context with a source of {@link TemperatureSources#PASSIVE}
     */
    TemperatureChange passive();

    /**
     * @return Returns a shared temperature change context with a source of {@link TemperatureSources#ENVIRONMENT}
     */
    TemperatureChange environment();
}