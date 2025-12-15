package com.github.thedeathlycow.thermoo.gametest.tick;

import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import com.github.thedeathlycow.thermoo.gametest.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRule;
import org.jetbrains.annotations.NotNull;

public class TestTemperatureChanges {
    /**
     * Gamerule to enable/disable passive changes for testing purposes
     */
    public static final GameRule<@NotNull Boolean> APPLY_PASSIVE_CHANGES =
            GameRuleBuilder.forBoolean(true)
                            .buildAndRegister(ThermooTestMod.id("applyPassiveChanges"));

    /**
     * Gamerule to enable/disable active changes for testing purposes
     */
    public static final GameRule<@NotNull Boolean> APPLY_ACTIVE_CHANGES =
            GameRuleBuilder.forBoolean(true)
                    .buildAndRegister(ThermooTestMod.id("applyActiveChanges"));

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
        LivingEntityTemperatureTickEvents.ALLOW_PASSIVE_TEMPERATURE_UPDATE.register(context -> {
            boolean applyPassiveChanges = context.level().getGameRules().get(APPLY_PASSIVE_CHANGES);
            return TriState.of(applyPassiveChanges);
        });
        LivingEntityTemperatureTickEvents.GET_PASSIVE_TEMPERATURE_CHANGE.register(TestTemperatureChanges::getPassiveChange);
        LivingEntityTemperatureTickEvents.ALLOW_PASSIVE_TEMPERATURE_CHANGE.register((context, temperatureChange) -> {
            if (context.affected().getType() == EntityType.PLAYER && context.affected().tickCount % 20 == 0) {
                Thermoo.LOGGER.info("Applying passive temperature change of {} to player", temperatureChange);
            }

            return TriState.DEFAULT;
        });

        LivingEntityTemperatureTickEvents.ALLOW_ACTIVE_TEMPERATURE_UPDATE.register(context -> {
            boolean applyActiveChanges = context.level().getGameRules().get(APPLY_ACTIVE_CHANGES);
            return TriState.of(applyActiveChanges);
        });
        LivingEntityTemperatureTickEvents.GET_ACTIVE_TEMPERATURE_CHANGE.register(TestTemperatureChanges::getActiveChange);
        LivingEntityTemperatureTickEvents.ALLOW_ACTIVE_TEMPERATURE_CHANGE.register((context, temperatureChange) -> {
            if (context.affected().getType() == EntityType.PLAYER && context.affected().tickCount % 20 == 0) {
                Thermoo.LOGGER.info("Applying active temperature change of {} to player", temperatureChange);
            }

            return TriState.DEFAULT;
        });
    }
}