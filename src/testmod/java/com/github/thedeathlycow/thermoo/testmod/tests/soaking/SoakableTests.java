package com.github.thedeathlycow.thermoo.testmod.tests.soaking;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class SoakableTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void entity_max_wet_ticks_is_600(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);
        context.assertEquals(villager.thermoo$getMaxWetTicks(), 600, "Max Wet Ticks");
        context.complete();
    }
}