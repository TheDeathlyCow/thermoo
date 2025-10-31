package com.github.thedeathlycow.thermoo.api.item;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.ApiStatus;

/**
 * Stack-aware event for modifying the default attribute modifier component of an item. This should only be used
 * for items external to your mod, such as vanilla items or items from other mods.
 * <p>
 * This does not modify the actual {@linkplain net.minecraft.core.component.DataComponents#ATTRIBUTE_MODIFIERS attribute modifiers component},
 * instead it adjusts the attributes that are used when applied to an entity or displaying the tooltip.
 * <p>
 * This is an experimental event, it may not work fully as expected or impact performance. Proceed with caution.
 * <p>
 * <strong>Example:</strong>
 * This listener adds max health to all helmets
 * <pre>
 * {@code
 *  ModifyItemAttributeModifiersCallback.EVENT.register((stack, builder) -> {
 *  	if (stack.is(ItemTags.HEAD_ARMOR)) {
 *          builder.add(Attributes.MAX_HEALTH, MODIFIER, EquipmentSlotGroup.HEAD);
 *      }
 *  });
 * }
 * </pre>
 */
@ApiStatus.Experimental
@FunctionalInterface
public interface ModifyItemAttributeModifiersCallback {
    Event<ModifyItemAttributeModifiersCallback> EVENT = EventFactory.createArrayBacked(
            ModifyItemAttributeModifiersCallback.class,
            listeners -> (stack, builder) -> {
                for (ModifyItemAttributeModifiersCallback listener : listeners) {
                    listener.modifyAttributeModifiers(stack, builder);
                }
            }
    );

    void modifyAttributeModifiers(ItemStack stack, ItemAttributeModifiers.Builder builder);
}