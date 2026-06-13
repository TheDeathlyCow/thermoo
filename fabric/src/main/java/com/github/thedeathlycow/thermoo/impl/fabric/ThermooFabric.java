package com.github.thedeathlycow.thermoo.impl.fabric;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentModInitializer;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Arrays;
import java.util.List;

public class ThermooFabric implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        Thermoo.onInitialize(mod);
        initializeDependentEntryPoints();
    }

    private static void initializeDependentEntryPoints() {
        List<DependentModInitializer> initializers = FabricLoader.getInstance().getEntrypoints(
                DependentModInitializer.ID,
                DependentModInitializer.class
        );

        for (DependentModInitializer initializer : initializers) {
            boolean initialize = Arrays.stream(initializer.getRequiredModIds()).allMatch(
                    id -> FabricLoader.getInstance().isModLoaded(id)
            );

            if (initialize) {
                initializer.onInitialize();
            }
        }
    }
}