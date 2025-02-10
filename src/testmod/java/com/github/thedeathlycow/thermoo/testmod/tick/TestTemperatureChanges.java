package com.github.thedeathlycow.thermoo.testmod.tick;

import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.TemperatureTickContext;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import com.github.thedeathlycow.thermoo.testmod.config.ThermooConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.LightType;

public class TestTemperatureChanges implements ModInitializer {
    /**
     * Gamerule to enable/disable passive changes for testing purposes
     */
    public static final GameRules.Key<GameRules.BooleanRule> APPLY_PASSIVE_CHANGES =
            GameRuleRegistry.register(
                    Thermoo.MODID + ".applyPassiveChanges",
                    GameRules.Category.MISC,
                    GameRuleFactory.createBooleanRule(true)
            );

    /**
     * Gamerule to enable/disable active changes for testing purposes
     */
    public static final GameRules.Key<GameRules.BooleanRule> APPLY_ACTIVE_CHANGES =
            GameRuleRegistry.register(
                    Thermoo.MODID + ".applyActiveChanges",
                    GameRules.Category.MISC,
                    GameRuleFactory.createBooleanRule(true)
            );

    public static int getActiveChange(TemperatureTickContext<LivingEntity> context) {
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

    public static int getPassiveChange(TemperatureTickContext<LivingEntity> context) {
        LivingEntity affected = context.affected();
        ServerWorld world = context.world();
        BlockPos pos = context.pos();
        int total = 0;

        BlockState state = context.affected().getBlockStateAtPos();
        if (state.isOf(Blocks.MAGMA_BLOCK)) {
            total += 12;

            if (affected.getType() == EntityType.PLAYER && affected.age % 20 == 0) {
                Thermoo.LOGGER.info("player is stepping on magma");
            }
        }

        int lightLevel = world.getLightLevel(LightType.BLOCK, pos);
        if (lightLevel >= 5) {
            total += 2 * (lightLevel - 5);
        }

        return total;
    }

    @Override
    public void onInitialize() {
        LivingEntityTemperatureTickEvents.ALLOW_PASSIVE_TEMPERATURE_UPDATE.register(context -> {
            boolean applyPassiveChanges = context.world().getGameRules().getBoolean(APPLY_PASSIVE_CHANGES);
            return TriState.of(applyPassiveChanges);
        });
        LivingEntityTemperatureTickEvents.GET_PASSIVE_TEMPERATURE_CHANGE.register(TestTemperatureChanges::getPassiveChange);
        LivingEntityTemperatureTickEvents.ALLOW_PASSIVE_TEMPERATURE_CHANGE.register((context, temperatureChange) -> {
            if (context.affected().getType() == EntityType.PLAYER && context.affected().age % 20 == 0) {
                Thermoo.LOGGER.info("Applying passive temperature change of {} to player", temperatureChange);
            }

            return TriState.DEFAULT;
        });

        LivingEntityTemperatureTickEvents.ALLOW_ACTIVE_TEMPERATURE_UPDATE.register(context -> {
            boolean applyActiveChanges = context.world().getGameRules().getBoolean(APPLY_ACTIVE_CHANGES);
            return TriState.of(applyActiveChanges);
        });
        LivingEntityTemperatureTickEvents.GET_ACTIVE_TEMPERATURE_CHANGE.register(TestTemperatureChanges::getActiveChange);
        LivingEntityTemperatureTickEvents.ALLOW_ACTIVE_TEMPERATURE_CHANGE.register((context, temperatureChange) -> {
            if (context.affected().getType() == EntityType.PLAYER && context.affected().age % 20 == 0) {
                Thermoo.LOGGER.info("Applying active temperature change of {} to player", temperatureChange);
            }

            return TriState.DEFAULT;
        });
    }
}