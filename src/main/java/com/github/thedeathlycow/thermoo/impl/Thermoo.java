package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.attribute.ItemAttributeModifier;
import com.github.thedeathlycow.thermoo.api.command.EnvironmentCommand;
import com.github.thedeathlycow.thermoo.api.command.HeatingModeArgumentType;
import com.github.thedeathlycow.thermoo.api.command.TemperatureCommand;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Thermoo implements ModInitializer {

    public static final String MODID = "thermoo";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
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

        DynamicRegistries.registerSynced(
                ThermooRegistryKeys.ITEM_ATTRIBUTE_MODIFIER,
                ItemAttributeModifier.CODEC,
                DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY
        );

        ItemAttributeModifierManager.INSTANCE.registerToEventsCommon();

        ThermooCommonRegisters.registerAttributes();
        ThermooCommonRegisters.registerTemperatureEffects();

        ResourceManagerHelper serverManager = ResourceManagerHelper.get(ResourceType.SERVER_DATA);

        serverManager.registerReloadListener(TemperatureEffectLoader.INSTANCE);
        LOGGER.info("Creating environment manager {}", EnvironmentManager.INSTANCE);
        LOGGER.info("Thermoo initialized");
    }

    /**
     * Creates a new {@link Identifier} under the namespace {@value #MODID}
     *
     * @param path The identifier path
     * @return Returns a new {@link Identifier}
     */
    @Contract("->new")
    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
