package com.github.thedeathlycow.thermoo.gametest.tests.soaking;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class SoakableTests {
    @GameTest
    public void entity_max_wet_ticks_is_600(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);
        context.assertEquals(villager.thermoo$getMaxWetTicks(), 600, Text.literal("Max Wet Ticks"));
        context.complete();
    }
}