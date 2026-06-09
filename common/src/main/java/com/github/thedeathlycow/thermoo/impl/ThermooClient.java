package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.impl.client.ThermooClientRegisters;
import com.github.thedeathlycow.thermoo.impl.compat.ThermooPatchesNag;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentClientModInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ThermooClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ThermooPatchesNag.initialize(Thermoo.getConfig());
        initializeDependentEntryPoints();

        ThermooClientRegisters.registerDebugEntries();
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
