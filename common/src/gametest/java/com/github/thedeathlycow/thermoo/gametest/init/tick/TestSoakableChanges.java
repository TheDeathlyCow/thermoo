/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.gametest.init.tick;

import com.github.thedeathlycow.thermoo.api.core.v2.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.core.v2.event.LivingEntitySoakingTickEvents;
import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import dev.yumi.commons.TriState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gamerules.GameRule;

public class TestSoakableChanges {
    /**
     * Gamerule to enable/disable soaking changes for testing purposes
     */
    public static final GameRule<Boolean> ALLOW_SOAKING_UPDATES = ThermooServices.GAME_RULES.forBoolean(
            ThermooTestMod.id("allow_soak_updates"),
            true
    );

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
            return TriState.from(applyPassiveChanges);
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