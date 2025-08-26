package com.github.thedeathlycow.thermoo.impl.compat.init;

public interface DependentModInitializer {
    String ID = "thermoo-dependent-mod";

    void onInitialize();

    String[] getRequiredModIds();
}