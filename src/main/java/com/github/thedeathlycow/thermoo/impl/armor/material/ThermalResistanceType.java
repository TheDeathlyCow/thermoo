package com.github.thedeathlycow.thermoo.impl.armor.material;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialEvents;
import com.github.thedeathlycow.thermoo.api.armor.material.ThermalResistanceLevel;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum ThermalResistanceType {

    COLD(
            ArmorMaterialEvents.GET_FROST_RESISTANCE,
            ThermooAttributes.FROST_RESISTANCE,
            Thermoo.id("armor.frost_resistance")
    ) {
        @Override
        protected TagKey<ArmorMaterial> getTagForLevel(ThermalResistanceLevel level) {
            return level.getColdTag();
        }
    },
    HEAT(
            ArmorMaterialEvents.GET_HEAT_RESISTANCE,
            ThermooAttributes.HEAT_RESISTANCE,
            Thermoo.id("armor.heat_resistance")
    ) {
        @Override
        protected TagKey<ArmorMaterial> getTagForLevel(ThermalResistanceLevel level) {
            return level.getHeatTag();
        }
    };
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
        double resistanceValue = this.getResistanceValue(armorMaterial);

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
        }
    }

    @Nullable
    protected abstract TagKey<ArmorMaterial> getTagForLevel(ThermalResistanceLevel level);

    private double getResistanceValue(RegistryEntry<ArmorMaterial> armorMaterial) {
        ThermalResistanceLevel level = this.getResistanceLevel(armorMaterial);
        return this.event.invoker().getValue(level);
    }

    private ThermalResistanceLevel getResistanceLevel(RegistryEntry<ArmorMaterial> armorMaterial) {
        for (ThermalResistanceLevel level : ThermalResistanceLevel.values()) {
            TagKey<ArmorMaterial> tag = this.getTagForLevel(level);
            if (tag != null && armorMaterial.isIn(tag)) {
                return level;
            }
        }

        return ThermalResistanceLevel.NEUTRAL;
    }
}
