package com.github.thedeathlycow.thermoo.impl;

import net.fabricmc.api.ClientModInitializer;

public class ThermooClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ItemAttributeModifierManager.registerToEventsClient(new ItemAttributeModifierManager());
    }
}
