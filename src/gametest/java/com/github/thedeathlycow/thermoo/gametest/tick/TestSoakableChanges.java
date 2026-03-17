package com.github.thedeathlycow.thermoo.gametest.tick;

import com.github.thedeathlycow.thermoo.api.core.v1.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.core.v1.event.LivingEntitySoakingTickEvents;
import com.github.thedeathlycow.thermoo.gametest.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gamerules.GameRule;
import org.jetbrains.annotations.NotNull;

public class TestSoakableChanges {
    /**
     * Gamerule to enable/disable soaking changes for testing purposes
     */
    public static final GameRule<@NotNull Boolean> ALLOW_SOAKING_UPDATES =
            GameRuleBuilder.forBoolean(true)
                            .buildAndRegister(ThermooTestMod.id("allow_soak_updates"));

    public static int addSoakingChange(EnvironmentTickContext<? extends LivingEntity> context) {
        LivingEntity entity = context.affected();
        int total = 0;

        if (entity.isInWater() || entity.getInBlockState().is(Blocks.WATER_CAULDRON)) {
            total += 5;
        }

        if (entity.isUnderWater()) {
            total = entity.thermoo$getMaxWetTicks();
        }

        if (entity.isOnFire()) {
            total -= 50;
        }

        return total;
    }

    public static void initialize() {
        LivingEntitySoakingTickEvents.ALLOW_SOAKING_UPDATE.register(context -> {
            boolean applyPassiveChanges = context.level().getGameRules().get(ALLOW_SOAKING_UPDATES);
            return TriState.of(applyPassiveChanges);
        });
        LivingEntitySoakingTickEvents.GET_SOAKING_CHANGE.register(TestSoakableChanges::addSoakingChange);
        LivingEntitySoakingTickEvents.ALLOW_SOAKING_CHANGE.register((context, soakingChange) -> {
            if (context.affected().getType() == EntityType.PLAYER && context.affected().tickCount % 20 == 0) {
                Thermoo.LOGGER.info("Applying soaking change of {} to player", soakingChange);
            }

            return TriState.DEFAULT;
        });
    }
}