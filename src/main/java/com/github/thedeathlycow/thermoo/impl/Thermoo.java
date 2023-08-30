package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.command.EnvironmentCommand;
import com.github.thedeathlycow.thermoo.api.command.HeatingModeArgumentType;
import com.github.thedeathlycow.thermoo.api.command.TemperatureCommand;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
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

        ArgumentTypeRegistry.registerArgumentType(
                Thermoo.id("heating_mode"),
                HeatingModeArgumentType.class,
                ConstantArgumentSerializer.of(HeatingModeArgumentType::heatingMode)
        );

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    dispatcher.register(TemperatureCommand.COMMAND_BUILDER.get());
                    dispatcher.register(EnvironmentCommand.COMMAND_BUILDER.get());
                }
        );

        ThermooCommonRegisters.registerAttributes();
        ThermooCommonRegisters.registerTemperatureEffects();

        ResourceManagerHelper serverManager = ResourceManagerHelper.get(ResourceType.SERVER_DATA);

        serverManager.registerReloadListener(TemperatureEffectLoader.INSTANCE);
        LOGGER.info("Creating environment manager {}", EnvironmentManager.INSTANCE);
        LOGGER.info("Thermoo initialized");
    }

    public static ThermooConfig getConfig() {
        return AutoConfig.getConfigHolder(ThermooConfig.class).getConfig();
    }

    /**
     * Creates a new {@link Identifier} under the namespace {@value MODID}
     *
     * @param path The identifier path
     * @return Returns a new {@link Identifier}
     */
    @Contract("->new")
    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
