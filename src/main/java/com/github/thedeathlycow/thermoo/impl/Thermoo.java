package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Thermoo implements ModInitializer {

    public static final String MODID = "thermoo";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final int CONFIG_VERSION = 0;

    @Override
    public void onInitialize() {
        AutoConfig.register(ThermooConfig.class, GsonConfigSerializer::new);
        ThermooConfig.updateConfig(AutoConfig.getConfigHolder(ThermooConfig.class));

        ThermooCommonRegisters.registerAttributes();
        ThermooCommonRegisters.registerTemperatureEffects();

        ResourceManagerHelper serverManager = ResourceManagerHelper.get(ResourceType.SERVER_DATA);

        serverManager.registerReloadListener(TemperatureEffectLoader.INSTANCE);

        LOGGER.info("Thermoo initialized");
    }

    public static ThermooConfig getConfig() {
        return AutoConfig.getConfigHolder(ThermooConfig.class).getConfig();
    }

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
