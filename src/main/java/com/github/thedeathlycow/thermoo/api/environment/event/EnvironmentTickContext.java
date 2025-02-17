package com.github.thedeathlycow.thermoo.api.environment.event;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.temperature.event.TickContext;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import org.jetbrains.annotations.ApiStatus;

/**
 * Context objects for environmental temperature ticking events on temperature awares
 *
 * @param <T> The temperature aware type
 */
@ApiStatus.NonExtendable
public interface EnvironmentTickContext<T extends TemperatureAware & Soakable> extends TickContext<T> {
    /**
     * The current environment components at the world and position.
     * <p>
     * This map is only non-empty for events such as {@link ServerPlayerEnvironmentTickEvents#GET_TEMPERATURE_CHANGE}
     * and later events. Even after this event, no key is guaranteed to be mapped to a value, be sure to always check the
     * result or use {@link ComponentMap#getOrDefault(ComponentType, Object)}.
     *
     * @return Returns an {@link EnvironmentComponentTypes environment component map}
     */
    ComponentMap components();
}
