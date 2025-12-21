package com.github.thedeathlycow.thermoo.testmod.tests;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@SuppressWarnings("unused")
@GameTestHolder(ThermooTestMod.MODID)
@PrefixGameTestTemplate(false)
public class AttributeTests {

    @GameTest(template = ThermooTestMod.EMPTY_STRUCTURE)
    public void villager_min_temperature_is_set_by_event(GameTestHelper context) {
        Villager villager = context.spawn(EntityType.VILLAGER, BlockPos.ZERO.above());

        context.assertValueEqual(villager.getAttributeValue(ThermooAttributes.MIN_TEMPERATURE), 40.0, "Min Temperature Attribute");
        context.assertValueEqual(villager.thermoo$getMinTemperature(), -40 * 140, "Min Temperature Value");
        context.succeed();
    }

    @GameTest(template = ThermooTestMod.EMPTY_STRUCTURE)
    public void villager_max_temperature_is_set_by_event(GameTestHelper context) {
        Villager villager = context.spawn(EntityType.VILLAGER, BlockPos.ZERO.above());

        context.assertValueEqual(villager.getAttributeValue(ThermooAttributes.MAX_TEMPERATURE), 40.0, "Max Temperature Attribute");
        context.assertValueEqual(villager.thermoo$getMaxTemperature(), 40 * 140, "Max Temperature Value");
        context.succeed();
    }
}