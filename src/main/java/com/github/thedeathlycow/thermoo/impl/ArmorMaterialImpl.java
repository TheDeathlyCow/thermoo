package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialEvents;
import com.github.thedeathlycow.thermoo.api.armor.material.ThermalResistanceLevel;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.util.OptionalDouble;
import java.util.function.Function;

public class ArmorMaterialImpl {

    public ThermalResistanceLevel getResistanceLevel(
            RegistryEntry<ArmorMaterial> armorMaterial,
            Function<ThermalResistanceLevel, TagKey<ArmorMaterial>> tagProvider
    ) {
        for (ThermalResistanceLevel level : ThermalResistanceLevel.values()) {
            TagKey<ArmorMaterial> tag = tagProvider.apply(level);
            if (armorMaterial.isIn(tag)) {
                return level;
            }
        }

        return ThermalResistanceLevel.NEUTRAL;
    }

    public OptionalDouble getResistance(
            ThermalResistanceLevel resistanceLevel,
            Event<ArmorMaterialEvents.GetResistance> event
    ) {
        return event.invoker().getValue(resistanceLevel);
    }
}
