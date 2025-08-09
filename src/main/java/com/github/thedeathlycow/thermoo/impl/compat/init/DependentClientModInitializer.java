package com.github.thedeathlycow.thermoo.impl.compat.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface DependentClientModInitializer {
    String ID = DependentModInitializer.ID + "-client";

    void onInitializeClient();

    String[] getRequiredModIds();
}