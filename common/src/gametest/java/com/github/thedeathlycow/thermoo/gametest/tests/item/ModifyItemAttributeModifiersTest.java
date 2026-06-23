/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
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

package com.github.thedeathlycow.thermoo.gametest.tests.item;

import com.github.thedeathlycow.thermoo.api.item.v2.ModifyItemAttributeModifiersCallback;
import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestMod;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;

@SuppressWarnings("unused")
public class ModifyItemAttributeModifiersTest {
    public static void initialize() {
        ModifyItemAttributeModifiersCallback.EVENT.register((stack, builder) -> {
            if (stack.is(Items.DIAMOND_CHESTPLATE)) {
                builder.add(
                        Attributes.SCALE,
                        new AttributeModifier(
                                ThermooTestMod.id("diamond_chestplate_scale_test"),
                                1.0,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.CHEST
                );
            }

            if (stack.is(ItemTags.AXES)) {
                builder.add(
                        Attributes.ARMOR,
                        new AttributeModifier(
                                ThermooTestMod.id("diamond_axe_armor_test"),
                                1.0,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                );
            }

            if (stack.is(Items.NETHERITE_AXE)) {
                builder.add(
                        Attributes.ARMOR,
                        // duplicate
                        new AttributeModifier(
                                ThermooTestMod.id("diamond_axe_armor_test"),
                                5.0,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                );
            }
        });
    }

    @GameTest
    public void defaultDiamondChestplateAppliesScale(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        helper.assertEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getScale, 1f);

        villager.setItemSlot(EquipmentSlot.CHEST, Items.DIAMOND_CHESTPLATE.getDefaultInstance());
        helper.succeedWhenEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getScale, 2f);
    }

    @GameTest
    public void defaultDiamondChestplateDoesNotApplyScaleWhenHeld(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        helper.assertEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getScale, 1f);

        villager.setItemInHand(InteractionHand.MAIN_HAND, Items.DIAMOND_CHESTPLATE.getDefaultInstance());
        helper.succeedWhenEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getScale, 1f);
    }

    @GameTest
    public void modifiedDiamondChestplateDoesNotApplyScale(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        helper.assertEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getScale, 1f);

        ItemStack stack = Items.DIAMOND_CHESTPLATE.getDefaultInstance();
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        villager.setItemSlot(EquipmentSlot.CHEST, stack);
        helper.succeedWhenEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getScale, 1f);
    }

    @GameTest
    public void defaultDiamondAxeAppliesArmor(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        helper.assertEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getArmorValue, 0);

        villager.setItemInHand(InteractionHand.MAIN_HAND, Items.DIAMOND_AXE.getDefaultInstance());
        helper.succeedWhenEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getArmorValue, 1);
    }

    @GameTest
    public void defaultNetheriteAxeOverwritesArmor(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        helper.assertEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getArmorValue, 0);

        villager.setItemInHand(InteractionHand.MAIN_HAND, Items.NETHERITE_AXE.getDefaultInstance());
        helper.succeedWhenEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getArmorValue, 5);
    }

    @GameTest
    public void defaultDiamondAxeDoesNotApplyArmorWhenWorn(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        helper.assertEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getArmorValue, 0);

        villager.setItemSlot(EquipmentSlot.HEAD, Items.DIAMOND_AXE.getDefaultInstance());
        helper.succeedWhenEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getArmorValue, 0);
    }

    @GameTest
    public void modifiedDiamondAxeDoesNotApplyArmor(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        helper.assertEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getArmorValue, 0);

        ItemStack stack = Items.DIAMOND_AXE.getDefaultInstance();
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        villager.setItemSlot(EquipmentSlot.CHEST, stack);
        helper.succeedWhenEntityData(BlockPos.ZERO, EntityType.VILLAGER, LivingEntity::getArmorValue, 0);
    }
}