package com.github.thedeathlycow.thermoo.impl.item;

import com.github.thedeathlycow.thermoo.api.item.ModifyItemAttributeModifiersCallback;
import com.github.thedeathlycow.thermoo.mixin.common.accessor.AttributeModifiersComponentBuilderAccessor;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModifyItemAttributeModifiersImpl {
    public static AttributeModifiersComponent invoke(ItemStack stack, AttributeModifiersComponent base) {
        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
        AttributeModifiersComponentBuilderAccessor accessor = (AttributeModifiersComponentBuilderAccessor) builder;
        accessor.scorchful$getEntries().addAll(base.modifiers());

        ModifyItemAttributeModifiersCallback.EVENT.invoker().modifyAttributeModifiers(stack, builder);

        return new AttributeModifiersComponent(removeDuplicates(builder.build().modifiers()));
    }

    private static List<AttributeModifiersComponent.Entry> removeDuplicates(Collection<AttributeModifiersComponent.Entry> modifiers) {
        Map<Pair<RegistryKey<Attribute>, Identifier>, AttributeModifiersComponent.Entry> map = new LinkedHashMap<>();

        // de-duplicates the modifiers to remove any entries that modify the same attribute and have the same ID
        for (var modifier : modifiers) {
            Pair<RegistryKey<Attribute>, Identifier> key = Pair.of(
                    modifier.attribute().getKey().orElseThrow(),
                    modifier.modifier().id()
            );
            map.put(key, modifier);
        }

        return map.values().stream().toList();
    }

    private ModifyItemAttributeModifiersImpl() {

    }
}