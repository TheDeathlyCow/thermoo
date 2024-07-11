package com.github.thedeathlycow.thermoo.api.armor.material;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ArmorMaterial;

import java.util.OptionalDouble;

public class ArmorMaterialEvents {

    public static final Event<GetResistance> GET_HEAT_RESISTANCE = EventFactory.createArrayBacked(
            GetResistance.class,
            listeners -> resistanceLevel -> {
                for (GetResistance listener : listeners) {
                    OptionalDouble value = listener.getValue(resistanceLevel);
                    if (value.isPresent()) {
                        return value;
                    }
                }

                return OptionalDouble.empty();
            }
    );

    public static final Event<GetResistance> GET_FROST_RESISTANCE = EventFactory.createArrayBacked(
            GetResistance.class,
            listeners -> resistanceLevel -> {
                for (GetResistance listener : listeners) {
                    OptionalDouble value = listener.getValue(resistanceLevel);
                    if (value.isPresent()) {
                        return value;
                    }
                }

                return OptionalDouble.empty();
            }
    );

    @FunctionalInterface
    public interface GetResistance {

        OptionalDouble getValue(ThermalResistanceLevel resistanceLevel);

    }

}
