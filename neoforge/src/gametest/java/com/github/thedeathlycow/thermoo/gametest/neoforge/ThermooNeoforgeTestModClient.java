package com.github.thedeathlycow.thermoo.gametest.neoforge;

import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestModClient;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;

public class ThermooNeoforgeTestModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        ThermooTestModClient.onInitializeClient();
    }
}