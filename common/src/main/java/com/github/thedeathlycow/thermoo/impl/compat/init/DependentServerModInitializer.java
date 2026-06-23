package com.github.thedeathlycow.thermoo.impl.compat.init;

public interface DependentServerModInitializer {
    String ID = DependentModInitializer.ID + "-server";

    void onInitializeServer();

    String[] getRequiredModIds();
}
