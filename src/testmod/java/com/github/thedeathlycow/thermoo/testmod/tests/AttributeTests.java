package com.github.thedeathlycow.thermoo.testmod.tests;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class AttributeTests {

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void villager_min_temperature_is_40(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);

        context.assertEquals(villager.getAttributeValue(ThermooAttributes.MIN_TEMPERATURE), 40, "Min Temperature Attribute");
        context.assertEquals(villager.thermoo$getMinTemperature(), -40 * 140, "Min Temperature Value");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void villager_max_temperature_is_40(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);

        context.assertEquals(villager.getAttributeValue(ThermooAttributes.MAX_TEMPERATURE), 40, "Max Temperature Attribute");
        context.assertEquals(villager.thermoo$getMaxTemperature(), 40 * 140, "Max Temperature Value");
        context.complete();
    }
}