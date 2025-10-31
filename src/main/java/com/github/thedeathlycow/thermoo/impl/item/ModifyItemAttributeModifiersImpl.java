package com.github.thedeathlycow.thermoo.impl.item;

import com.github.thedeathlycow.thermoo.api.item.ModifyItemAttributeModifiersCallback;
import com.github.thedeathlycow.thermoo.mixin.common.accessor.AttributeModifiersComponentBuilderAccessor;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
        Map<Pair<ResourceKey<Attribute>, ResourceLocation>, ItemAttributeModifiers.Entry> map = new LinkedHashMap<>();

        // de-duplicates the modifiers to remove any entries that modify the same attribute and have the same ID
        for (var modifier : modifiers) {
            Pair<ResourceKey<Attribute>, ResourceLocation> key = Pair.of(
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