package com.github.thedeathlycow.thermoo.impl.client;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugEnvironmentComponents;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugEnvironments;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugSeasons;
import com.github.thedeathlycow.thermoo.impl.client.debug.DebugSelfStatuses;
import com.github.thedeathlycow.thermoo.mixin.client.DebugScreenEntriesAccessor;

public final class ThermooClientRegisters {
    public static void registerDebugEntries() {
        DebugScreenEntriesAccessor.thermoo_invokeRegister(Thermoo.id("environment_components"), new DebugEnvironmentComponents());
        DebugScreenEntriesAccessor.thermoo_invokeRegister(Thermoo.id("environments"), new DebugEnvironments());
        DebugScreenEntriesAccessor.thermoo_invokeRegister(Thermoo.id("seasons"), new DebugSeasons());
        DebugScreenEntriesAccessor.thermoo_invokeRegister(Thermoo.id("self_status"), new DebugSelfStatuses());
    }

    private ThermooClientRegisters() {

    }
}