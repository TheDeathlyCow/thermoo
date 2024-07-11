package com.github.thedeathlycow.thermoo.api.armor.material;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ArmorMaterialEvents {

    public static final Event<GetResistance> GET_HEAT_RESISTANCE = EventFactory.createArrayBacked(
            GetResistance.class,
            listeners -> resistanceLevel -> {
                for (GetResistance listener : listeners) {
                    double value = listener.getValue(resistanceLevel);
                    if (value != 0 && !Double.isNaN(value)) {
                        return value;
                    }
                }

                return Double.NaN;
            }
    );

    public static final Event<GetResistance> GET_FROST_RESISTANCE = EventFactory.createArrayBacked(
            GetResistance.class,
            listeners -> resistanceLevel -> {
                for (GetResistance listener : listeners) {
                    double value = listener.getValue(resistanceLevel);
                    if (value != 0 && !Double.isNaN(value)) {
                        return value;
                    }
                }

                return Double.NaN;
            }
    );

    @FunctionalInterface
    public interface GetResistance {

        double getValue(ThermalResistanceLevel resistanceLevel);

    }

}
