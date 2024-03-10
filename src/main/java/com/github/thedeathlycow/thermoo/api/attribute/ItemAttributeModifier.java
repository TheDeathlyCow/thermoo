package com.github.thedeathlycow.thermoo.api.attribute;

import com.github.thedeathlycow.thermoo.api.ThermooCodecs;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.util.dynamic.Codecs;

public record ItemAttributeModifier(
        EntityAttribute attribute,
        EntityAttributeModifier modifier,
        ItemPredicate itemTest,
        EquipmentSlot slot
) {

    public static final Codec<ItemAttributeModifier> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Registries.ATTRIBUTE.getCodec()
                            .fieldOf("attribute")
                            .forGetter(ItemAttributeModifier::attribute),
                    ThermooCodecs.ATTRIBUTE_MODIFIER_CODEC
                            .fieldOf("modifier")
                            .forGetter(ItemAttributeModifier::modifier),
                    ThermooCodecs.ITEM_PREDICATE_CODEC
                            .fieldOf("item")
                            .forGetter(ItemAttributeModifier::itemTest),
                    ThermooCodecs.EQUIPMENT_SLOT_CODEC
                            .fieldOf("slot")
                            .forGetter(ItemAttributeModifier::slot)
            ).apply(instance, ItemAttributeModifier::new)
    );

    public void apply(
            ItemStack stack,
            EquipmentSlot slot,
            Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers
    ) {
        if (this.slot == slot && this.itemTest.test(stack)) {
            attributeModifiers.put(this.attribute, this.modifier);
        }
    }
}
