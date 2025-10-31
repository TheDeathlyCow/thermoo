package com.github.thedeathlycow.thermoo.gametest.tests.soaking;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;

@SuppressWarnings("unused")
public class SoakableTests {
    @GameTest
    public void entity_max_wet_ticks_is_600(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        helper.assertValueEqual(villager.thermoo$getMaxWetTicks(), 600, Component.literal("Max Wet Ticks"));
        helper.succeed();
    }
}