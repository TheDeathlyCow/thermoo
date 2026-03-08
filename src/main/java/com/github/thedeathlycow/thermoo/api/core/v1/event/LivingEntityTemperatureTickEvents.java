package com.github.thedeathlycow.thermoo.api.core.v1.event;

import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.impl.core.UpdateEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;

/**
 * Events for ticking passive and active temperature changes on entities on the logical server. These events will apply
 * to spectator entities, but will not apply to dead or removed entities.
 * <p>
 * There are two categories of temperature change update ticks: passive and active. Passive changes should be used for
 * temperature changes from nearby blocks, such as heat from light sources or cooling from an air conditioner. Active
 * changes should be used for temperature changes from entity effects such as heat from being on fire, or cold from
 * being submerged in powder snow.
 * <p>
 * For environmental effects, see the {@link com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition} that handles
 * the environment datapack registries, and {@link com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents}
 * for applying temperature changes from those environmental conditions.
 * <p>
 * The events are invoked in the following order:
 * <ul><li>ALLOW_(PASSIVE|ACTIVE)_TEMPERATURE_UPDATE
 * <li>GET_(PASSIVE|ACTIVE)_TEMPERATURE_CHANGE
 * <li>ALLOW_(PASSIVE|ACTIVE)_TEMPERATURE_CHANGE</ul>
 */
public final class LivingEntityTemperatureTickEvents {
    public static Event<GetTemperatureChange> getTemperatureChange(ResourceKey<TemperatureSource> sourceKey) {
        return UpdateEvents.getOrCreate(sourceKey).getChange();
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