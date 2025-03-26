package com.github.thedeathlycow.thermoo.gametest.tests;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class AttributeTests {

    @GameTest
    public void villager_min_temperature_is_set_by_event(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN.up());

        context.assertEquals(villager.getAttributeValue(ThermooAttributes.MIN_TEMPERATURE), 40.0, Text.literal("Min Temperature Attribute"));
        context.assertEquals(villager.thermoo$getMinTemperature(), -40 * 140, Text.literal("Min Temperature Value"));
        context.complete();
    }

    @GameTest
    public void villager_max_temperature_is_set_by_event(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN.up());

        context.assertEquals(villager.getAttributeValue(ThermooAttributes.MAX_TEMPERATURE), 40.0, Text.literal("Max Temperature Attribute"));
        context.assertEquals(villager.thermoo$getMaxTemperature(), 40 * 140, Text.literal("Max Temperature Value"));
        context.complete();
    }
}