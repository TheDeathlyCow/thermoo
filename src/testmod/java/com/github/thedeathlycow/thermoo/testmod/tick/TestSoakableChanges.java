package com.github.thedeathlycow.thermoo.testmod.tick;

import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntitySoakingTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.GameRules;

public class TestSoakableChanges {
    /**
     * Gamerule to enable/disable soaking changes for testing purposes
     */
    public static final GameRules.Key<GameRules.BooleanRule> ALLOW_SOAKING_UPDATES =
            GameRuleRegistry.register(
                    Thermoo.MODID + ".allowSoakingUpdates",
                    GameRules.Category.MISC,
                    GameRuleFactory.createBooleanRule(true)
            );

    public static int addSoakingChange(EnvironmentTickContext<? extends LivingEntity> context) {
        LivingEntity entity = context.affected();
        int total = 0;

        if (entity.isTouchingWater() || entity.getBlockStateAtPos().isOf(Blocks.WATER_CAULDRON)) {
            total += 5;
        }

        if (entity.isSubmergedInWater()) {
            total = entity.thermoo$getMaxWetTicks();
        }

        if (entity.isOnFire()) {
            total -= 50;
        }

        return total;
    }

    public static void initialize() {
        LivingEntitySoakingTickEvents.ALLOW_SOAKING_UPDATE.register(context -> {
            boolean applyPassiveChanges = context.world().getGameRules().getBoolean(ALLOW_SOAKING_UPDATES);
            return TriState.of(applyPassiveChanges);
        });
        LivingEntitySoakingTickEvents.GET_SOAKING_CHANGE.register(TestSoakableChanges::addSoakingChange);
        LivingEntitySoakingTickEvents.ALLOW_SOAKING_CHANGE.register((context, soakingChange) -> {
            if (context.affected().getType() == EntityType.PLAYER && context.affected().age % 20 == 0) {
                Thermoo.LOGGER.info("Applying soaking change of {} to player", soakingChange);
            }

            return TriState.DEFAULT;
        });
    }
}