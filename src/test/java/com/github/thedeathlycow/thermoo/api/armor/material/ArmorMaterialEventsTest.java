package com.github.thedeathlycow.thermoo.api.armor.material;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class ArmorMaterialEventsTest {

    @BeforeAll
    static void setup() {
        ArmorMaterialEvents.GET_FROST_RESISTANCE.register(
                (armorMaterial, armorType) -> armorType == ArmorItem.Type.HELMET ? 1 : Double.NaN
        );
        ArmorMaterialEvents.GET_HEAT_RESISTANCE.register(
                (armorMaterial, armorType) -> armorType == ArmorItem.Type.HELMET ? 1 : Double.NaN
        );
    }

    @Test
    void noListenersRegistered_noFrostResistance() {
        var stack = Items.DIAMOND_BOOTS.getDefaultInstance();

        final var frostResistance = ThermooAttributes.FROST_RESISTANCE.unwrapKey().orElse(null);
        Set<ResourceKey<Attribute>> attributeKeys = new HashSet<>();

        stack.forEachModifier(EquipmentSlot.FEET, (attribute, modifier) -> {
            attributeKeys.add(attribute.unwrapKey().orElse(null));
        });

        Assertions.assertFalse(attributeKeys.contains(frostResistance));
    }

    @Test
    void listenersRegistered_containsFrostResistance() {
        var stack = Items.DIAMOND_HELMET.getDefaultInstance();

        final var frostResistance = ThermooAttributes.FROST_RESISTANCE.unwrapKey().orElse(null);
        Set<ResourceKey<Attribute>> attributeKeys = new HashSet<>();

        stack.forEachModifier(EquipmentSlot.HEAD, (attribute, modifier) -> {
            attributeKeys.add(attribute.unwrapKey().orElse(null));
        });

        Assertions.assertTrue(attributeKeys.contains(frostResistance));
    }

    @Test
    void noListenersRegistered_noHeatResistance() {
        var stack = Items.DIAMOND_BOOTS.getDefaultInstance();

        final var heatResistance = ThermooAttributes.HEAT_RESISTANCE.unwrapKey().orElse(null);
        Set<ResourceKey<Attribute>> attributeKeys = new HashSet<>();

        stack.forEachModifier(EquipmentSlot.FEET, (attribute, modifier) -> {
            attributeKeys.add(attribute.unwrapKey().orElse(null));
        });

        Assertions.assertFalse(attributeKeys.contains(heatResistance));
    }

    @Test
    void listenersRegistered_containsHeatResistance() {
        var stack = Items.DIAMOND_HELMET.getDefaultInstance();

        final var heatResistance = ThermooAttributes.HEAT_RESISTANCE.unwrapKey().orElse(null);
        Set<ResourceKey<Attribute>> attributeKeys = new HashSet<>();

        stack.forEachModifier(EquipmentSlot.HEAD, (attribute, modifier) -> {
            attributeKeys.add(attribute.unwrapKey().orElse(null));
        });

        Assertions.assertTrue(attributeKeys.contains(heatResistance));
    }

}
