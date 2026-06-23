package com.github.thedeathlycow.thermoo.impl.fabric;

import com.github.thedeathlycow.thermoo.impl.ThermooClient;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentClientModInitializer;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Arrays;
import java.util.List;

public class ThermooFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        ThermooClient.onInitializeClient(mod);
        initializeDependentEntryPoints();
    }

    private static void initializeDependentEntryPoints() {
        List<DependentClientModInitializer> initializers = FabricLoader.getInstance().getEntrypoints(
                DependentClientModInitializer.ID,
                DependentClientModInitializer.class
        );

        for (DependentClientModInitializer initializer : initializers) {
            boolean initialize = Arrays.stream(initializer.getRequiredModIds()).allMatch(
                    id -> FabricLoader.getInstance().isModLoaded(id)
            );

            if (initialize) {
                initializer.onInitializeClient();
            }
        }
    }
}