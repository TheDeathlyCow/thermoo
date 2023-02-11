package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.biome.Biome;

public final class PlayerEnvironmentEvents {

    private PlayerEnvironmentEvents() {
    }

    public static final Event<BiomeTemperatureChangeTick> WARM_BIOME_TICK = EventFactory.createArrayBacked(BiomeTemperatureChangeTick.class,
            callbacks -> (controller, player, biome, temperatureChange, result) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = player.world.getProfiler();
                    profiler.push("thermooWarmBiomeTemperatureChangeTick");

                    for (BiomeTemperatureChangeTick event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onTemperatureChangeTick(controller, player, biome, temperatureChange, result);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (BiomeTemperatureChangeTick event : callbacks) {
                        event.onTemperatureChangeTick(controller, player, biome, temperatureChange, result);
                    }
                }
            }
    );

    public static final Event<BiomeTemperatureChangeTick> COLD_BIOME_TICK = EventFactory.createArrayBacked(BiomeTemperatureChangeTick.class,
            callbacks -> (controller, player, biome, temperatureChange, result) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = player.world.getProfiler();
                    profiler.push("thermooColdBiomeTemperatureChangeTick");

                    for (BiomeTemperatureChangeTick event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onTemperatureChangeTick(controller, player, biome, temperatureChange, result);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (BiomeTemperatureChangeTick event : callbacks) {
                        event.onTemperatureChangeTick(controller, player, biome, temperatureChange, result);
                    }
                }
            }
    );

    public static final Event<TemperateBiomeTemperatureChangeTick> TEMPERATE_BIOME_TICK = EventFactory.createArrayBacked(TemperateBiomeTemperatureChangeTick.class,
            callbacks -> (controller, player, biome) -> {
                if (EventFactory.isProfilingEnabled()) {
                    final Profiler profiler = player.world.getProfiler();
                    profiler.push("thermooWarmBiomeTemperatureChangeTick");

                    for (TemperateBiomeTemperatureChangeTick event : callbacks) {
                        profiler.push(EventFactory.getHandlerName(event));
                        event.onTemperatureChangeTick(controller, player, biome);
                        profiler.pop();
                    }

                    profiler.pop();
                } else {
                    for (TemperateBiomeTemperatureChangeTick event : callbacks) {
                        event.onTemperatureChangeTick(controller, player, biome);
                    }
                }
            }
    );

    @FunctionalInterface
    public interface HeatSourcesTemperatureChangeTick {

        void onTemperatureChangeTick(EnvironmentController controller, LivingEntity entity, int temperatureChange);

    }

    @FunctionalInterface
    public interface BiomeTemperatureChangeTick {
        void onTemperatureChangeTick(EnvironmentController controller, PlayerEntity player, Biome biome, int temperatureChange, EnvironmentChangeResult result);
    }

    @FunctionalInterface
    public interface TemperateBiomeTemperatureChangeTick {
        void onTemperatureChangeTick(EnvironmentController controller, PlayerEntity player, Biome biome);
    }

}
