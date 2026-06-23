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

package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.impl.item.ModifyItemAttributeModifiersImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.function.TriConsumer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract DataComponentMap getComponents();

    @Shadow
    @Final
    private PatchedDataComponentMap components;

    @WrapOperation(
            method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;forEach(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V"
            )
    )
    private void hookItemModifierEvent(
            ItemAttributeModifiers instance,
            EquipmentSlotGroup slot,
            TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> attributeConsumer,
            Operation<Void> original
    ) {
        // prevent overriding modified components from commands
        if (!this.components.hasNonDefault(DataComponents.ATTRIBUTE_MODIFIERS)) {
            instance = ModifyItemAttributeModifiersImpl.invoke((ItemStack) (Object) this, instance);
        }
        original.call(instance, slot, attributeConsumer);
    }


    @WrapOperation(
            method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;forEach(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"
            )
    )
    private void hookItemModifierEvent(
            ItemAttributeModifiers instance,
            EquipmentSlot slot,
            BiConsumer<Holder<Attribute>, AttributeModifier> attributeConsumer,
            Operation<Void> original
    ) {
        // prevent overriding modified components from commands
        if (!this.components.hasNonDefault(DataComponents.ATTRIBUTE_MODIFIERS)) {
            instance = ModifyItemAttributeModifiersImpl.invoke((ItemStack) (Object) this, instance);
        }
        original.call(instance, slot, attributeConsumer);
    }
}