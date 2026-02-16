package com.github.thedeathlycow.thermoo.impl.client;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugEnvironmentComponents;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugEnvironments;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugSeasons;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;

public final class ThermooClientRegisters {
    public static void registerDebugEntries() {
        DebugScreenEntries.register(Thermoo.id("environment_components"), new DebugEnvironmentComponents());
        DebugScreenEntries.register(Thermoo.id("environments"), new DebugEnvironments());
        DebugScreenEntries.register(Thermoo.id("seasons"), new DebugSeasons());
    }

    private ThermooClientRegisters() {

    }
}