package com.github.thedeathlycow.thermoo.api.armor.material;

import com.github.thedeathlycow.thermoo.ThermooTest;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class ArmorMaterialEventsTest {

    @BeforeAll
    static void setup() {
        ThermooTest.bootstrapRegistries();

        ArmorMaterialEvents.GET_FROST_RESISTANCE.register(
                (armorMaterial, armorType) -> armorType == ArmorItem.Type.HELMET ? 1 : Double.NaN
        );
        ArmorMaterialEvents.GET_HEAT_RESISTANCE.register(
                (armorMaterial, armorType) -> armorType == ArmorItem.Type.HELMET ? 1 : Double.NaN
        );
    }

    @Test
    void noListenersRegistered_noFrostResistance() {
        var stack = Items.DIAMOND_BOOTS.getDefaultStack();

        final var frostResistance = ThermooAttributes.FROST_RESISTANCE.getKey().orElse(null);
        Set<RegistryKey<EntityAttribute>> attributeKeys = new HashSet<>();

        stack.applyAttributeModifiers(EquipmentSlot.FEET, (attribute, modifier) -> {
            attributeKeys.add(attribute.getKey().orElse(null));
        });

        Assertions.assertFalse(attributeKeys.contains(frostResistance));
    }

    @Test
    void listenersRegistered_containsFrostResistance() {
        var stack = Items.DIAMOND_HELMET.getDefaultStack();

        final var frostResistance = ThermooAttributes.FROST_RESISTANCE.getKey().orElse(null);
        Set<RegistryKey<EntityAttribute>> attributeKeys = new HashSet<>();

        stack.applyAttributeModifiers(EquipmentSlot.HEAD, (attribute, modifier) -> {
            attributeKeys.add(attribute.getKey().orElse(null));
        });

        Assertions.assertTrue(attributeKeys.contains(frostResistance));
    }

    @Test
    void noListenersRegistered_noHeatResistance() {
        var stack = Items.DIAMOND_BOOTS.getDefaultStack();

        final var heatResistance = ThermooAttributes.HEAT_RESISTANCE.getKey().orElse(null);
        Set<RegistryKey<EntityAttribute>> attributeKeys = new HashSet<>();

        stack.applyAttributeModifiers(EquipmentSlot.FEET, (attribute, modifier) -> {
            attributeKeys.add(attribute.getKey().orElse(null));
        });

        Assertions.assertFalse(attributeKeys.contains(heatResistance));
    }

    @Test
    void listenersRegistered_containsHeatResistance() {
        var stack = Items.DIAMOND_HELMET.getDefaultStack();

        final var heatResistance = ThermooAttributes.HEAT_RESISTANCE.getKey().orElse(null);
        Set<RegistryKey<EntityAttribute>> attributeKeys = new HashSet<>();

        stack.applyAttributeModifiers(EquipmentSlot.HEAD, (attribute, modifier) -> {
            attributeKeys.add(attribute.getKey().orElse(null));
        });

        Assertions.assertTrue(attributeKeys.contains(heatResistance));
    }

}
