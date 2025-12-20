package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.impl.compat.ThermooPatchesNag;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentClientModInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

import java.util.Arrays;
import java.util.List;


@Mod(value = Thermoo.MODID, dist = Dist.CLIENT)
public class ThermooClient {
    public ThermooClient() {
    }

//    private static void initializeDependentEntryPoints() {
//        List<DependentClientModInitializer> initializers = FabricLoader.getInstance().getEntrypoints(
//                DependentClientModInitializer.ID,
//                DependentClientModInitializer.class
//        );
//
//        for (DependentClientModInitializer initializer : initializers) {
//            boolean initialize = Arrays.stream(initializer.getRequiredModIds()).allMatch(
//                    id -> FabricLoader.getInstance().isModLoaded(id)
//            );
//
//            if (initialize) {
//                initializer.onInitializeClient();
//            }
//        }
//    }
}
