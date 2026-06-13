package com.github.thedeathlycow.thermoo.impl.fabric;

import com.github.thedeathlycow.thermoo.impl.compat.init.DependentServerModInitializer;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.server.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Arrays;
import java.util.List;

public class ThermooFabricServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeDedicatedServer(ModContainer mod) {
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