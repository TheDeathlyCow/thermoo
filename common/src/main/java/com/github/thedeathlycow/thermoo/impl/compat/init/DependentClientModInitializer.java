package com.github.thedeathlycow.thermoo.impl.compat.init;

public interface DependentClientModInitializer {
    String ID = DependentModInitializer.ID + "-client";

    void onInitializeClient();

    String[] getRequiredModIds();
}