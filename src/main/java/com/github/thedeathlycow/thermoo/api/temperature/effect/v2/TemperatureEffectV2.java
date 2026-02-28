package com.github.thedeathlycow.thermoo.api.temperature.effect.v2;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Range;

public interface TemperatureEffectV2 {
    int DEFAULT_INTERVAL = 1;

    boolean apply(LivingEntity victim, Level level);

    default void remove(LivingEntity victim, Level level) {
    }

    @Range(from = 1, to = Integer.MAX_VALUE)
    int interval();

    MapCodec<? extends TemperatureEffectV2> codec();
}
