package com.github.thedeathlycow.thermoo.api.armor.material;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * Events for getting the thermal resistances of various {@linkplain ArmorMaterial armor materials}
 */
public class ArmorMaterialEvents {

    /**
     * Gets the {@linkplain com.github.thedeathlycow.thermoo.api.ThermooAttributes#HEAT_RESISTANCE heat resistance}
     * for a particular {@linkplain ArmorMaterial armor material} and {@linkplain ArmorItem.Type armor type}.
     * <p>
     * If returns 0 or {@link Double#NaN} then no heat resistance is applied.
     * <p>
     * It is recommended to base the resistance amount on the material {@linkplain ArmorMaterialTags tag}.
     * <p>
     * Known issue: using tags is not world-specific, so please do not use these tags in datapacks that aren't meant to
     * be applied globally.
     */
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

    /**
     * Gets the {@linkplain com.github.thedeathlycow.thermoo.api.ThermooAttributes#FROST_RESISTANCE frost resistance}
     * for a particular {@linkplain ArmorMaterial armor material} and {@linkplain ArmorItem.Type armor type}.
     * <p>
     * If returns 0 or {@link Double#NaN} then no frost resistance is applied.
     * <p>
     * It is recommended to base the resistance amount on the material {@linkplain ArmorMaterialTags tag}.
     * <p>
     * Known issue: using tags is not world-specific, so please do not use these tags in datapacks that aren't meant to
     * be applied globally.
     */
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
