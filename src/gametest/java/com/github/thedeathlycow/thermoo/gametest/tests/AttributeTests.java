package com.github.thedeathlycow.thermoo.gametest.tests;

import com.github.thedeathlycow.thermoo.api.entity.v1.ThermooAttributes;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;

@SuppressWarnings("unused")
public class AttributeTests {

    @GameTest
    public void villager_min_temperature_is_set_by_event(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO.above());

        helper.assertValueEqual(villager.getAttributeValue(ThermooAttributes.MIN_TEMPERATURE), 40.0, Component.literal("Min Temperature Attribute"));
        helper.assertValueEqual(villager.thermoo$getMinTemperature(), -40 * 140, Component.literal("Min Temperature Value"));
        helper.succeed();
    }

    @GameTest
    public void villager_max_temperature_is_set_by_event(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO.above());

        helper.assertValueEqual(villager.getAttributeValue(ThermooAttributes.MAX_TEMPERATURE), 40.0, Component.literal("Max Temperature Attribute"));
        helper.assertValueEqual(villager.thermoo$getMaxTemperature(), 40 * 140, Component.literal("Max Temperature Value"));
        helper.succeed();
    }
}