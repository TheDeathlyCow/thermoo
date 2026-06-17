package com.github.thedeathlycow.thermoo.impl.neoforge;

import com.github.thedeathlycow.thermoo.impl.ThermooClient;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;

public class ThermooNeoforgeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        ThermooClient.onInitializeClient(mod);
    }
}