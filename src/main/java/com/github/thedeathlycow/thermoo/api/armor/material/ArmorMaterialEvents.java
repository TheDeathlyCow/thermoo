package com.github.thedeathlycow.thermoo.api.armor.material;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;

public class ArmorMaterialEvents {

    public static final Event<GetResistance> GET_HEAT_RESISTANCE = EventFactory.createArrayBacked(
            GetResistance.class,
            listeners -> (armorMaterial, armorType) -> {
                for (GetResistance listener : listeners) {
                    double value = listener.getValue(armorMaterial, armorType);
                    if (value != 0 && !Double.isNaN(value)) {
                        return value;
                    }
                }

                return Double.NaN;
            }
    );

    public static final Event<GetResistance> GET_FROST_RESISTANCE = EventFactory.createArrayBacked(
            GetResistance.class,
            listeners -> (armorMaterial, armorType) -> {
                for (GetResistance listener : listeners) {
                    double value = listener.getValue(armorMaterial, armorType);
                    if (value != 0 && !Double.isNaN(value)) {
                        return value;
                    }
                }

                return Double.NaN;
            }
    );

    @FunctionalInterface
    public interface GetResistance {

        double getValue(RegistryEntry<ArmorMaterial> armorMaterial, ArmorItem.Type armorType);

    }

}
