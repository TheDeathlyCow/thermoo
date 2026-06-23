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

package com.github.thedeathlycow.thermoo.api.item.v2;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.commons.event.Event;
import net.minecraft.resources.Identifier;
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
    Event<Identifier, ModifyItemAttributeModifiersCallback> EVENT = Thermoo.EVENT_MANAGER.create(
            ModifyItemAttributeModifiersCallback.class,
            listeners -> (stack, builder) -> {
                for (ModifyItemAttributeModifiersCallback listener : listeners) {
                    listener.modifyAttributeModifiers(stack, builder);
                }
            }
    );

    void modifyAttributeModifiers(ItemStack stack, ItemAttributeModifiers.Builder builder);
}