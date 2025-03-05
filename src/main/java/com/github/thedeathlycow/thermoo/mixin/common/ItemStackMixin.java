package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.impl.item.ModifyItemAttributeModifiersImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract ComponentChanges getComponentChanges();

    @WrapOperation(
            method = "applyAttributeModifier",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/component/type/AttributeModifiersComponent;applyModifiers(Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V"
            )
    )
    private void hookItemModifierEvent(
            AttributeModifiersComponent instance,
            AttributeModifierSlot slot,
            BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeConsumer,
            Operation<Void> original
    ) {
        // prevent overriding modified components from commands
        if (this.getComponentChanges().get(DataComponentTypes.ATTRIBUTE_MODIFIERS) == null) {
            instance = ModifyItemAttributeModifiersImpl.invoke((ItemStack) (Object) this, instance);
        }
        original.call(instance, slot, attributeConsumer);
    }


    @WrapOperation(
            method = "applyAttributeModifiers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/component/type/AttributeModifiersComponent;applyModifiers(Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"
            )
    )
    private void hookItemModifierEvent(
            AttributeModifiersComponent instance,
            EquipmentSlot slot,
            BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeConsumer,
            Operation<Void> original
    ) {
        // prevent overriding modified components from commands
        if (this.getComponentChanges().get(DataComponentTypes.ATTRIBUTE_MODIFIERS) == null) {
            instance = ModifyItemAttributeModifiersImpl.invoke((ItemStack) (Object) this, instance);
        }
        original.call(instance, slot, attributeConsumer);
    }
}