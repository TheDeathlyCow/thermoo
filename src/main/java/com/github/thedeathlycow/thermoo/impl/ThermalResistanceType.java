package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public enum ThermalResistanceType {

    COLD(
            ArmorMaterialEvents.GET_FROST_RESISTANCE,
            ThermooAttributes.FROST_RESISTANCE,
            Thermoo.id("armor.frost_resistance")
    ),
    HEAT(
            ArmorMaterialEvents.GET_HEAT_RESISTANCE,
            ThermooAttributes.HEAT_RESISTANCE,
            Thermoo.id("armor.heat_resistance")
    );
    private final Event<ArmorMaterialEvents.GetResistance> event;
    private final RegistryEntry<EntityAttribute> attribute;
    private final Identifier modifierId;

    ThermalResistanceType(
            Event<ArmorMaterialEvents.GetResistance> event,
            RegistryEntry<EntityAttribute> attribute,
            Identifier modifierId
    ) {
        this.event = event;
        this.attribute = attribute;
        this.modifierId = modifierId;
    }

    public void buildResistance(
            RegistryEntry<ArmorMaterial> armorMaterial,
            ArmorItem.Type type,
            AttributeModifiersComponent.Builder builder
    ) {
        double resistanceValue = this.getResistanceValue(armorMaterial, type);

        if (resistanceValue != 0 && !Double.isNaN(resistanceValue)) {
            builder.add(
                    attribute,
                    new EntityAttributeModifier(
                            this.modifierId,
                            resistanceValue,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    ),
                    AttributeModifierSlot.forEquipmentSlot(type.getEquipmentSlot())
            );
            if (Thermoo.LOGGER.isDebugEnabled()) {
                Thermoo.LOGGER.debug("Applying {} {} to armor material {}", resistanceValue, attribute, armorMaterial);
            }
        }
    }

    private double getResistanceValue(RegistryEntry<ArmorMaterial> armorMaterial, ArmorItem.Type type) {
        return this.event.invoker().getValue(armorMaterial, type);
    }
}
