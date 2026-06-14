package com.github.thedeathlycow.thermoo.gametest.init.tick;

import com.github.thedeathlycow.thermoo.api.core.v2.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.core.v2.event.LivingEntityTemperatureTickEvents;
import com.github.thedeathlycow.thermoo.api.core.v2.event.TemperatureChangeEvents;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSources;
import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import dev.yumi.commons.TriState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRule;

public class TestTemperatureChanges {
    /**
     * Gamerule to enable/disable passive changes for testing purposes
     */
    public static final GameRule<Boolean> APPLY_PASSIVE_CHANGES = ThermooServices.GAME_RULES.forBoolean(
            ThermooTestMod.id("apply_passive_changes"),
            true
    );

    /**
     * Gamerule to enable/disable active changes for testing purposes
     */
    public static final GameRule<Boolean> APPLY_ACTIVE_CHANGES = ThermooServices.GAME_RULES.forBoolean(
            ThermooTestMod.id("apply_active_changes"),
            true
    );

    public static int getActiveChange(EnvironmentTickContext<? extends LivingEntity> context) {
        LivingEntity affected = context.affected();
        int total = 0;

        if (affected.isOnFire()) {
            total += 50;
        }

        if (affected.wasInPowderSnow) {
            total -= 50;
        }

        return total;
    }

    public static int getPassiveChange(EnvironmentTickContext<? extends LivingEntity> context) {
        LivingEntity affected = context.affected();
        ServerLevel world = context.level();
        BlockPos pos = context.pos();
        int total = 0;

        BlockState state = context.affected().getBlockStateOn();
        if (state.is(Blocks.MAGMA_BLOCK)) {
            total += 12;

            if (affected.getType() == EntityType.PLAYER && affected.tickCount % 20 == 0) {
                Thermoo.LOGGER.info("player is stepping on magma");
            }
        }

        int lightLevel = world.getBrightness(LightLayer.BLOCK, pos);
        if (lightLevel >= 5) {
            total += 2 * (lightLevel - 5);
        }

        return total;
    }

    public static void initialize() {
        LivingEntityTemperatureTickEvents.getTemperatureChange(
                TemperatureSources.PASSIVE
        ).register(TestTemperatureChanges::getPassiveChange);

        LivingEntityTemperatureTickEvents.getTemperatureChange(
                TemperatureSources.ACTIVE
        ).register(TestTemperatureChanges::getActiveChange);

        TemperatureChangeEvents.ALLOW_TEMPERATURE_CHANGE.register((target, _, _, context) -> {
            if (target.level() instanceof ServerLevel serverLevel) {
                if (context.is(TemperatureSources.PASSIVE)) {
                    return TriState.from(serverLevel.getGameRules().get(APPLY_PASSIVE_CHANGES));
                }

                if (context.is(TemperatureSources.ACTIVE)) {
                    return TriState.from(serverLevel.getGameRules().get(APPLY_ACTIVE_CHANGES));
                }
            }

            return TriState.DEFAULT;
        });

        TemperatureChangeEvents.AFTER_TEMPERATURE_CHANGE.register((target, oldTemperature, newTemperature, ctx) -> {
            if (target.getType() == EntityType.PLAYER && target.tickCount % 20 == 0) {
                Thermoo.LOGGER.info(
                        "Player temperature updated from {} to {} (changed: {}) by {}",
                        oldTemperature, newTemperature,
                        newTemperature - oldTemperature,
                        ctx
                );
            }
        });
    }
}