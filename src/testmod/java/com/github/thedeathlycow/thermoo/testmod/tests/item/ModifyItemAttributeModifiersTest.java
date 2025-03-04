package com.github.thedeathlycow.thermoo.testmod.tests.item;

import com.github.thedeathlycow.thermoo.api.item.ModifyItemAttributeModifiersCallback;
import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class ModifyItemAttributeModifiersTest {
    public static void initialize() {
        ModifyItemAttributeModifiersCallback.EVENT.register((stack, builder) -> {
            if (stack.isOf(Items.DIAMOND_CHESTPLATE)) {
                builder.add(
                        EntityAttributes.SCALE,
                        new EntityAttributeModifier(
                                ThermooTestMod.id("diamond_chestplate_scale_test"),
                                1.0,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.CHEST
                );
            }

            if (stack.isIn(ItemTags.AXES)) {
                builder.add(
                        EntityAttributes.ARMOR,
                        new EntityAttributeModifier(
                                ThermooTestMod.id("diamond_axe_armor_test"),
                                1.0,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND
                );
            }

            if (stack.isOf(Items.NETHERITE_AXE)) {
                builder.add(
                        EntityAttributes.ARMOR,
                        // duplicate
                        new EntityAttributeModifier(
                                ThermooTestMod.id("diamond_axe_armor_test"),
                                5.0,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND
                );
            }
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void default_diamond_chestplate_applies_scale(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);
        context.expectEntityWithData(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getScale, 1f);

        villager.equipStack(EquipmentSlot.CHEST, Items.DIAMOND_CHESTPLATE.getDefaultStack());
        context.expectEntityWithDataEnd(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getScale, 2f);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void default_diamond_chestplate_does_not_apply_scale_when_held(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);
        context.expectEntityWithData(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getScale, 1f);

        villager.setStackInHand(Hand.MAIN_HAND, Items.DIAMOND_CHESTPLATE.getDefaultStack());
        context.expectEntityWithDataEnd(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getScale, 1f);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void modified_diamond_chestplate_does_not_apply_scale(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);
        context.expectEntityWithData(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getScale, 1f);

        ItemStack stack = Items.DIAMOND_CHESTPLATE.getDefaultStack();
        stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);

        villager.equipStack(EquipmentSlot.CHEST, stack);
        context.expectEntityWithDataEnd(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getScale, 1f);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void default_diamond_axe_applies_armor(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);
        context.expectEntityWithData(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getArmor, 0);

        villager.setStackInHand(Hand.MAIN_HAND, Items.DIAMOND_AXE.getDefaultStack());
        context.expectEntityWithDataEnd(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getArmor, 1);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void default_netherite_axe_overwrites_armor(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);
        context.expectEntityWithData(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getArmor, 0);

        villager.setStackInHand(Hand.MAIN_HAND, Items.NETHERITE_AXE.getDefaultStack());
        context.expectEntityWithDataEnd(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getArmor, 5);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void default_diamond_axe_does_not_apply_armor_when_worn(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);
        context.expectEntityWithData(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getArmor, 0);

        villager.equipStack(EquipmentSlot.HEAD, Items.DIAMOND_AXE.getDefaultStack());
        context.expectEntityWithDataEnd(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getArmor, 0);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void modified_diamond_axe_does_not_apply_armor(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, BlockPos.ORIGIN);
        context.expectEntityWithData(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getArmor, 0);

        ItemStack stack = Items.DIAMOND_AXE.getDefaultStack();
        stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);

        villager.equipStack(EquipmentSlot.CHEST, stack);
        context.expectEntityWithDataEnd(BlockPos.ORIGIN, EntityType.VILLAGER, LivingEntity::getArmor, 0);
    }
}