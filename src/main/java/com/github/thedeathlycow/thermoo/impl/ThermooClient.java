package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.impl.compat.ThermooPatchesNag;
import net.fabricmc.api.ClientModInitializer;

public class ThermooClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ThermooPatchesNag.initialize(Thermoo.getConfig());
    }
}
