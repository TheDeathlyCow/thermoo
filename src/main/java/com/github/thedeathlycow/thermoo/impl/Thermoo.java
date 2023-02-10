package com.github.thedeathlycow.thermoo.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Thermoo implements ModInitializer {

    public static final String MODID = "thermoo";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {

        ThermooCommonRegisters.registerAttributes();
        ThermooCommonRegisters.registerTemperatureEffects();

        ResourceManagerHelper serverManager = ResourceManagerHelper.get(ResourceType.SERVER_DATA);

        serverManager.registerReloadListener(TemperatureEffectLoader.INSTANCE);

        LOGGER.info("Thermoo initialized");
    }

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
