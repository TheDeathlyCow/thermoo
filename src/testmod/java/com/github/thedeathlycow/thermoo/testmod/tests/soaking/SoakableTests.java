package com.github.thedeathlycow.thermoo.testmod.tests.soaking;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;

@SuppressWarnings("unused")
public class SoakableTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void entity_max_wet_ticks_is_600(GameTestHelper context) {
        Villager villager = context.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        context.assertValueEqual(villager.thermoo$getMaxWetTicks(), 600, "Max Wet Ticks");
        context.succeed();
    }
}