package com.github.thedeathlycow.thermoo.impl.neoforge;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.neoforge.registry.ThermooAttachments;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;

public class ThermooNeoforge implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        Thermoo.onInitialize(mod);
        ThermooAttachments.initialize();
    }
}