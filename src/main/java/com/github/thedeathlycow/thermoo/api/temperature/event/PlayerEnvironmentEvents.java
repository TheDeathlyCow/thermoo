package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.biome.Biome;

public final class PlayerEnvironmentEvents {

    private PlayerEnvironmentEvents() {
    }

    public static final Event<BiomeTemperatureChangeTick> TICK_WARM_BIOME_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(BiomeTemperatureChangeTick.class,
            callbacks -> (controller, player, biome, temperatureChange, result) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = player.world.getProfiler();
                    profiler.push("thermooWarmBiomeTemperatureChangeTick");

                    for (BiomeTemperatureChangeTick event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onBiomeTemperatureChange(controller, player, biome, temperatureChange, result);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (BiomeTemperatureChangeTick event : callbacks) {
                        event.onBiomeTemperatureChange(controller, player, biome, temperatureChange, result);
                    }
                }
            }
    );

    public static final Event<BiomeTemperatureChangeTick> TICK_COLD_BIOME_TEMPERATURE_CHANGE = EventFactory.createArrayBacked(BiomeTemperatureChangeTick.class,
            callbacks -> (controller, player, biome, temperatureChange, result) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = player.world.getProfiler();
                    profiler.push("thermooColdBiomeTemperatureChangeTick");

                    for (BiomeTemperatureChangeTick event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onBiomeTemperatureChange(controller, player, biome, temperatureChange, result);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (BiomeTemperatureChangeTick event : callbacks) {
                        event.onBiomeTemperatureChange(controller, player, biome, temperatureChange, result);
                    }
                }
            }
    );

    @FunctionalInterface
    public interface BiomeTemperatureChangeTick {

        void onBiomeTemperatureChange(EnvironmentController controller, PlayerEntity player, Biome biome, int temperatureChange, EnvironmentChangeResult result);
        
    }

}
