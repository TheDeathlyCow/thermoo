package com.github.thedeathlycow.thermoo.api.temperature.effect.v2;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public sealed interface TemperatureEffect<L extends Level> permits ClientTemperatureEffect, ServerTemperatureEffect {
    boolean apply(LivingEntity victim, L level);

    void remove(LivingEntity victim, L level);
}
