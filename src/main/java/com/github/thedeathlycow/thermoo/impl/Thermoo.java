package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.command.*;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentModInitializer;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import com.github.thedeathlycow.thermoo.impl.temperature.effect.TemperatureEffectLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resource.ResourceType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Thermoo implements ModInitializer {
    public static final String MODID = "thermoo";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final ArgumentSerializer<
            HeatingModeArgumentType,
            SingletonArgumentInfo<HeatingModeArgumentType>.Properties
            > HEATING_MODE_ARG_SERIALIZER = SingletonArgumentInfo.contextAware(HeatingModeArgumentType::heatingMode);


    public static final ArgumentSerializer<
            TemperatureUnitArgumentType,
            SingletonArgumentInfo<TemperatureUnitArgumentType>.Properties
            > TEMPERATURE_UNIT_ARG_SERIALIZER = SingletonArgumentInfo.contextFree(TemperatureUnitArgumentType::temperatureUnit);

    @Nullable
    private static ThermooConfig config = null;

    @Override
    public void onInitialize() {
        ArgumentTypeRegistry.registerArgumentType(
                Thermoo.location("heating_mode"),
                HeatingModeArgumentType.class,
                HEATING_MODE_ARG_SERIALIZER
        );

        ArgumentTypeRegistry.registerArgumentType(
                Thermoo.location("temperature_unit"),
                TemperatureUnitArgumentType.class,
                TEMPERATURE_UNIT_ARG_SERIALIZER
        );

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    dispatcher.register(TemperatureCommand.COMMAND_BUILDER.get());
                    dispatcher.register(EnvironmentCommand.COMMAND_BUILDER.get());
                    dispatcher.register(SoakingCommand.COMMAND_BUILDER.get());
                }
        );

        DynamicRegistries.register(
                ThermooRegistryKeys.ENVIRONMENT,
                EnvironmentDefinition.CODEC
        );
        DynamicRegistries.register(
                ThermooRegistryKeys.ENVIRONMENT_PROVIDER,
                EnvironmentProvider.ELEMENT_CODEC
        );
        ThermooCommonRegisters.registerTemperatureEffects();
        ThermooCommonRegisters.registerEnvironmentProviderTypes();
        ThermooCommonRegisters.registerLootConditionTypes();

        ResourceManagerHelper serverManager = ResourceManagerHelper.get(PackType.SERVER_DATA);
        serverManager.registerReloadListener(TemperatureEffectLoader.ID, TemperatureEffectLoader::new);

        EnvironmentLookupImpl.initialize();

        initializeDependentEntryPoints();

        LOGGER.info("Thermoo initialized");
    }

    /**
     * Creates a new {@link ResourceLocation} under the namespace {@value #MODID}
     *
     * @param path The identifier path
     * @return Returns a new {@link ResourceLocation}
     */
    @Contract("_->new")
    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ThermooConfig getConfig() {
        if (config == null) {
            config = ThermooConfig.create();
        }
        return config;
    }

    private static void initializeDependentEntryPoints() {
        List<DependentModInitializer> initializers = FabricLoader.getInstance().getEntrypoints(
                DependentModInitializer.ID,
                DependentModInitializer.class
        );

        for (DependentModInitializer initializer : initializers) {
            boolean initialize = Arrays.stream(initializer.getRequiredModIds()).allMatch(
                    id -> FabricLoader.getInstance().isModLoaded(id)
            );

            if (initialize) {
                initializer.onInitialize();
            }
        }
    }
}
