package com.github.thedeathlycow.thermoo.api.attribute;

import com.github.thedeathlycow.thermoo.api.ThermooCodecs;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Applies changes to the default attributes of an item. Item attribute modifiers are provided in datapack JSON files at
 * {@code data/<entry namespace>/thermoo/item_attribute_modifier/<entry path>}. They are synchronized to clients so that
 * they will properly show up in the item tooltip.
 * <p>
 * Example JSON file:
 * <pre>
 *     {
 *          "attribute": "thermoo:generic.heat_resistance",
 *          "modifier": {
 *              "uuid": "413a10a0-bf0b-47db-a9a9-2eb3dda3bbaf",
 *              "name": "Test",
 *              "value": -1.0,
 *              "operation": "ADDITION"
 *          },
 *          "item": {
 *              "items": [
 *                  "minecraft:diamond_helmet",
 *                  "minecraft:iron_helmet",
 *                  "minecraft:leather_helmet"
 *              ]
 *          },
 *          "slot": "HELMET"
 *     }
 * </pre>
 * <p>
 * You can also use tags:
 * <pre>
 *     {
 *          "attribute": "thermoo:generic.heat_resistance",
 *          "modifier": {
 *              "uuid": "413a10a0-bf0b-47db-a9a9-2eb3dda3bbaf",
 *              "name": "Test",
 *              "value": 2.0,
 *              "operation": "ADDITION"
 *          },
 *          "item": {
 *              "tag": "scorchful:turtle_armor"
 *          },
 *          "slot": "HELMET"
 *     }
 * </pre>
 * <p>
 * This class is experimental and subject to change. Please use the datapack JSON instead of referencing this class directly.
 *
 * @param attribute     The attribute this modifier affects
 * @param modifier      The modifier that this applies to the attribute
 * @param itemPredicate A type predicate for items this should apply to
 * @param slot          The slot this should apply to
 */
@ApiStatus.Experimental
public record ItemAttributeModifier(
        EntityAttribute attribute,
        EntityAttributeModifier modifier,
        ItemTypePredicate itemPredicate,
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
                    ItemTypePredicate.CODEC
                            .fieldOf("item")
                            .forGetter(ItemAttributeModifier::itemPredicate),
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
        if (this.slot == slot && this.itemPredicate.test(stack)) {
            attributeModifiers.put(this.attribute, this.modifier);
        }
    }

    public record ItemTypePredicate(
            @Nullable List<Item> items,
            @Nullable TagKey<Item> itemTag
    ) implements Predicate<ItemStack> {

        public static final Codec<ItemTypePredicate> CODEC = Codec.<ItemTypePredicate, ItemTypePredicate>either(
                RecordCodecBuilder.create(
                        instance -> instance.group(
                                Codec.list(Registries.ITEM.getCodec())
                                        .fieldOf("items")
                                        .forGetter(ItemTypePredicate::items)
                        ).apply(instance, items -> new ItemTypePredicate(items, null))
                ),
                RecordCodecBuilder.create(
                        instance -> instance.group(
                                TagKey.codec(RegistryKeys.ITEM)
                                        .fieldOf("tag")
                                        .forGetter(ItemTypePredicate::itemTag)
                        ).apply(instance, tag -> new ItemTypePredicate(null, tag))
                )
        ).xmap(
                either -> either.left().orElseGet(() -> either.right().orElseThrow()),
                Either::left
        );

        @Override
        public boolean test(ItemStack stack) {

            if (this.items == null && this.itemTag == null) {
                return false;
            }

            if (this.items != null && !this.items.contains(stack.getItem())) {
                return false;
            }

            if (this.itemTag != null && !stack.isIn(this.itemTag)) {
                return false;
            }

            return true;
        }
    }
}
