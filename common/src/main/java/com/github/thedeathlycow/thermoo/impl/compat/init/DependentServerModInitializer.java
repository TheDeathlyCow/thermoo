package com.github.thedeathlycow.thermoo.impl.compat.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public interface DependentServerModInitializer {
    String ID = DependentModInitializer.ID + "-server";

    void onInitializeServer();

    String[] getRequiredModIds();
}
