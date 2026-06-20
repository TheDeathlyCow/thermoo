package com.github.thedeathlycow.thermoo.gametest.fabric;

import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestMod;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

public class ThermooFabricTestMod implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        ThermooTestMod.onInitialize();
    }
}