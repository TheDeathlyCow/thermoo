package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.biome.Biome;

/**
 * Events relevant to player ticking and passive temperature changes
 */
public final class PlayerEnvironmentEvents {

    private PlayerEnvironmentEvents() {
    }

    /**
     * Tick when the player is in a warm biome (local biome temperature > 0)
     */
    public static final Event<BiomeTemperatureChangeTickCallback> TICK_WARM_BIOME_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(BiomeTemperatureChangeTickCallback.class,
            callbacks -> (controller, player, biome, result) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = player.world.getProfiler();
                    profiler.push("thermooWarmBiomeTemperatureChangeTick");

                    for (BiomeTemperatureChangeTickCallback event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onBiomeTemperatureChange(controller, player, biome, result);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (BiomeTemperatureChangeTickCallback event : callbacks) {
                        event.onBiomeTemperatureChange(controller, player, biome, result);
                    }
                }
            }
    );

    /**
     * Tick when the player is in a cold biome (local biome temperature less than 0)
     */
    public static final Event<BiomeTemperatureChangeTickCallback> TICK_COLD_BIOME_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(BiomeTemperatureChangeTickCallback.class,
            callbacks -> (controller, player, biome, result) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = player.world.getProfiler();
                    profiler.push("thermooColdBiomeTemperatureChangeTick");

                    for (BiomeTemperatureChangeTickCallback event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onBiomeTemperatureChange(controller, player, biome, result);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (BiomeTemperatureChangeTickCallback event : callbacks) {
                        event.onBiomeTemperatureChange(controller, player, biome, result);
                    }
                }
            }
    );

    /**
     * Callback for passive temperature change ticks
     */
    @FunctionalInterface
    public interface BiomeTemperatureChangeTickCallback {

        /**
         * Invoked when the temperature change should be applied. Note that the change is NOT applied by this event,
         * listeners must apply it themselves.
         *
         * @param controller The {@link EnvironmentController} relevant to this event
         * @param player     The player being ticked
         * @param biome      The biome the player is in
         * @param result     Stores information about the change to be applied
         */
        void onBiomeTemperatureChange(EnvironmentController controller, PlayerEntity player, Biome biome, EnvironmentChangeResult result);

    }

}
