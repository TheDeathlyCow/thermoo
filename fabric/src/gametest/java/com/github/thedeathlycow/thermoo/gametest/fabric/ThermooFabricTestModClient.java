package com.github.thedeathlycow.thermoo.gametest.fabric;

import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestModClient;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;

public class ThermooFabricTestModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        ThermooTestModClient.onInitializeClient();
    }
}