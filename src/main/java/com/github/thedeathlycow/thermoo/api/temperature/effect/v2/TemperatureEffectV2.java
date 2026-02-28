package com.github.thedeathlycow.thermoo.api.temperature.effect.v2;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface TemperatureEffectV2 {
    boolean apply(LivingEntity victim, Level level);

    void remove(LivingEntity victim, Level level);

    MapCodec<? extends TemperatureEffectV2> codec();
}
