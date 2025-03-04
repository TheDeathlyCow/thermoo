package com.github.thedeathlycow.thermoo.api.item;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * Stack-aware event for modifying the default attribute modifier component of an item.
 * <p>
 * Experimental event, it may not work fully as expected or impact performance. Proceed with caution.
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

    void modifyAttributeModifiers(ItemStack stack, AttributeModifiersComponent.Builder builder);
}