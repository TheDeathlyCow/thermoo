package com.github.thedeathlycow.thermoo.impl.compat.polymer;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentServerModInitializer;

public class ThermooPolymerPatch implements DependentServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (Thermoo.getConfig().enablePolymerPatch()) {
            
        }
    }

    @Override
    public String[] getRequiredModIds() {
        return new String[]{"polymer-core"};
    }
}