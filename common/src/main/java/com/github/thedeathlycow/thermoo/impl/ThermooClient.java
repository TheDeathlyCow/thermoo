package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.impl.client.ThermooClientRegisters;
import com.github.thedeathlycow.thermoo.impl.compat.ThermooPatchesNag;
import dev.yumi.mc.core.api.ModContainer;

public final class ThermooClient {
    public static void onInitializeClient(ModContainer mod) {
        ThermooPatchesNag.initialize(Thermoo.getConfig());
        ThermooClientRegisters.registerDebugEntries();
    }

    private ThermooClient() {

    }
}
