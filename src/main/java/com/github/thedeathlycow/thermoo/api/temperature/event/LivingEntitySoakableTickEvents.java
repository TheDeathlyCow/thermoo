package com.github.thedeathlycow.thermoo.api.temperature.event;

public final class LivingEntitySoakableTickEvents {

    @FunctionalInterface
    public interface GetSoakingChange {
        int addSoakedTicks(LivingEntityTickContext context);
    }

    private LivingEntitySoakableTickEvents() {

    }
}