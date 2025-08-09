package com.github.thedeathlycow.thermoo.impl.compat.polymer;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.attribute.AttributeData;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentServerModInitializer;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;

public class ThermooPolymerPatch implements DependentServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (Thermoo.getConfig().enablePolymerPatch()) {
            polymerizeAttributes();

            Thermoo.LOGGER.info("Patched Thermoo for server-side with Polymer!");
        }
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