package com.github.thedeathlycow.thermoo.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EnvironmentTemperature {

    public static int getPassiveTemperatureDelta(PlayerEntity player) {
        return 0;
    }

    public static int getWarmthFromLight(LivingEntity entity) {
        return 0;
    }
}
