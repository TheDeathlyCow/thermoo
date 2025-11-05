package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.impl.item.ModifyItemAttributeModifiersImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract DataComponentPatch getComponentsPatch();

    @WrapOperation(
            method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;forEach(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V"
            )
    )
    private void hookItemModifierEvent(
            ItemAttributeModifiers instance,
            EquipmentSlotGroup slot,
            BiConsumer<Holder<Attribute>, AttributeModifier> attributeConsumer,
            Operation<Void> original
    ) {
        // prevent overriding modified components from commands
        if (this.getComponentsPatch().get(DataComponents.ATTRIBUTE_MODIFIERS) == null) {
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
        if (this.getComponentsPatch().get(DataComponents.ATTRIBUTE_MODIFIERS) == null) {
            instance = ModifyItemAttributeModifiersImpl.invoke((ItemStack) (Object) this, instance);
        }
        original.call(instance, slot, attributeConsumer);
    }
}