package com.github.thedeathlycow.thermoo.api.temperature.event;

public final class LivingEntityTemperatureTickEvents {

    @FunctionalInterface
    public interface PassiveTemperatureChange {
        int addPassiveHeatTicks(LivingEntityTickContext context);
    }

    @FunctionalInterface
    public interface ActiveTemperatureChange {
        int addActiveHeatTicks(LivingEntityTickContext context);
    }

    private LivingEntityTemperatureTickEvents() {

    }
}