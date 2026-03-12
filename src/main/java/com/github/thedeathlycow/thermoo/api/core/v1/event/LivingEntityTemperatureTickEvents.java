package com.github.thedeathlycow.thermoo.api.core.v1.event;

import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.impl.core.UpdateEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;

/**
 * Events for ticking temperature changes on living entities on the logical server. These events will not apply
 * to dead or removed entities.
 * <p>
 * For finer grained control over whether a temperature change is allowed to proceed or to react after a change has
 * been applied, see {@link TemperatureChangeEvents}.
 * <p>
 * For environmental effects, see {@link com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition} for
 * the environment changes, and
 * {@link com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents} for applying
 * temperature changes from environmental conditions.
 */
public final class LivingEntityTemperatureTickEvents {
    /**
     * Returns the temperature change event for a specific {@link TemperatureSource}. Each registered listener
     * contributes a temperature change value that is summed and applied to the entity via
     * {@link com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware#thermoo$addTemperature(int, com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange)}.
     * <p>
     * Note that this event is only invoked for sources that are registered to the level's ticking sources and have a
     * {@link TemperatureSource#tickInterval()} greater than {@code 0}.
     *
     * @param sourceKey The registry key of the temperature source to get the event for, may not be {@code null}.
     * @return Returns the event for the given source key, creating it if it does not already exist.
     */
    public static Event<GetTemperatureChange> getTemperatureChange(ResourceKey<TemperatureSource> sourceKey) {
        return UpdateEvents.getOrCreate(sourceKey).event();
    }

    @FunctionalInterface
    public interface GetTemperatureChange {
        /**
         * Calculates the temperature change that this listener wants to add to a living entity this tick.
         *
         * @param context Context of the living entity for the tick.
         * @return Return the temperature point change that this listener wants to apply to the entity in the context.
         * This value is added to the values supplied by the other listeners.
         */
        int addTemperature(EnvironmentTickContext<? extends LivingEntity> context);
    }

    private LivingEntityTemperatureTickEvents() {

    }
}