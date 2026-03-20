package com.github.thedeathlycow.thermoo.impl.client;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugEnvironmentComponents;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugEnvironments;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugSeasons;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugSelfStatuses;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;

@Environment(EnvType.CLIENT)
public final class ThermooClientRegisters {
    public static void registerDebugEntries() {
        DebugScreenEntries.register(Thermoo.id("environment_components"), new DebugEnvironmentComponents());
        DebugScreenEntries.register(Thermoo.id("environments"), new DebugEnvironments());
        DebugScreenEntries.register(Thermoo.id("seasons"), new DebugSeasons());
        DebugScreenEntries.register(Thermoo.id("self_status"), new DebugSelfStatuses());
    }

    private ThermooClientRegisters() {

    }
}