package com.github.thedeathlycow.thermoo.impl.compat.polymer;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.attribute.AttributeData;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentServerModInitializer;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import net.minecraft.core.registries.BuiltInRegistries;

public class ThermooPolymerPatch implements DependentServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (Thermoo.getConfig().enablePolymerPatch()) {
            polymerizeAttributes();
            polymerizeArgumentTypes();

            Thermoo.LOGGER.info("Patched Thermoo for server-side with Polymer!");
        }
    }

    private static void polymerizeArgumentTypes() {
        RegistrySyncUtils.setServerEntry(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, Thermoo.HEATING_MODE_ARG_SERIALIZER);
        RegistrySyncUtils.setServerEntry(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, Thermoo.TEMPERATURE_UNIT_ARG_SERIALIZER);
    }

    private static void polymerizeAttributes() {
        for (AttributeData data : AttributeData.values()) {
            PolymerEntityUtils.registerAttribute(data.attribute());
        }
    }

    @Override
    public String[] getRequiredModIds() {
        return new String[]{"polymer-core"};
    }
}