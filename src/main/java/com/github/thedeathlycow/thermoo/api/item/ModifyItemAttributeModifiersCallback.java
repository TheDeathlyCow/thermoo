package com.github.thedeathlycow.thermoo.api.item;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

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