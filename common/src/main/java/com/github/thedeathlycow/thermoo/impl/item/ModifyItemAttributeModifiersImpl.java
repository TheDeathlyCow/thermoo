/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
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

package com.github.thedeathlycow.thermoo.impl.item;

import com.github.thedeathlycow.thermoo.api.item.v2.ModifyItemAttributeModifiersCallback;
import com.github.thedeathlycow.thermoo.mixin.common.accessor.AttributeModifiersComponentBuilderAccessor;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModifyItemAttributeModifiersImpl {
    public static ItemAttributeModifiers invoke(ItemStack stack, ItemAttributeModifiers base) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        AttributeModifiersComponentBuilderAccessor accessor = (AttributeModifiersComponentBuilderAccessor) builder;
        accessor.scorchful$getEntries().addAll(base.modifiers());

        ModifyItemAttributeModifiersCallback.EVENT.invoker().modifyAttributeModifiers(stack, builder);

        return new ItemAttributeModifiers(removeDuplicates(builder.build().modifiers()));
    }

    private static List<ItemAttributeModifiers.Entry> removeDuplicates(Collection<ItemAttributeModifiers.Entry> modifiers) {
        Map<Pair<ResourceKey<Attribute>, Identifier>, ItemAttributeModifiers.Entry> map = new LinkedHashMap<>();

        // de-duplicates the modifiers to remove any entries that modify the same attribute and have the same ID
        for (var modifier : modifiers) {
            Pair<ResourceKey<Attribute>, Identifier> key = Pair.of(
                    modifier.attribute().unwrapKey().orElseThrow(),
                    modifier.modifier().id()
            );
            map.put(key, modifier);
        }

        return map.values().stream().toList();
    }

    private ModifyItemAttributeModifiersImpl() {

    }
}