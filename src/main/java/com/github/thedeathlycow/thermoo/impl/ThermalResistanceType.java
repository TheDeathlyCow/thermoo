package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public enum ThermalResistanceType {

    COLD(
            ArmorMaterialEvents.GET_FROST_RESISTANCE,
            ThermooAttributes.FROST_RESISTANCE,
            "armor.frost_resistance"
    ),
    HEAT(
            ArmorMaterialEvents.GET_HEAT_RESISTANCE,
            ThermooAttributes.HEAT_RESISTANCE,
            "armor.heat_resistance"
    );
    private final Event<ArmorMaterialEvents.GetResistance> event;
    private final Holder<Attribute> attribute;
    private final String modifierId;

    ThermalResistanceType(
            Event<ArmorMaterialEvents.GetResistance> event,
            Holder<Attribute> attribute,
            String modifierId
    ) {
        this.event = event;
        this.attribute = attribute;
        this.modifierId = modifierId;
    }

    public void buildResistance(
            Holder<ArmorMaterial> armorMaterial,
            ArmorItem.Type type,
            ItemAttributeModifiers.Builder builder
    ) {
        double resistanceValue = this.getResistanceValue(armorMaterial, type);

        if (resistanceValue != 0 && !Double.isNaN(resistanceValue)) {
            builder.add(
                    attribute,
                    new AttributeModifier(
                            Thermoo.id(this.modifierId + "." + type.getName()),
                            resistanceValue,
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.bySlot(type.getSlot())
            );
            if (Thermoo.LOGGER.isDebugEnabled()) {
                Thermoo.LOGGER.debug("Applying {} {} to armor material {}", resistanceValue, attribute, armorMaterial);
            }
        }
    }

    private double getResistanceValue(Holder<ArmorMaterial> armorMaterial, ArmorItem.Type type) {
        return this.event.invoker().getValue(armorMaterial, type);
    }
}
