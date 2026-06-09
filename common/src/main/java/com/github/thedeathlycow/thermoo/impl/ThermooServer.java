package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.impl.compat.init.DependentServerModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Arrays;
import java.util.List;

public class ThermooServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        initializeDependentEntryPoints();
    }

    private static void initializeDependentEntryPoints() {
        List<DependentServerModInitializer> initializers = FabricLoader.getInstance().getEntrypoints(
                DependentServerModInitializer.ID,
                DependentServerModInitializer.class
        );

        for (DependentServerModInitializer initializer : initializers) {
            boolean initialize = Arrays.stream(initializer.getRequiredModIds()).allMatch(
                    id -> FabricLoader.getInstance().isModLoaded(id)
            );

            if (initialize) {
                initializer.onInitializeServer();
            }
        }
    }
}