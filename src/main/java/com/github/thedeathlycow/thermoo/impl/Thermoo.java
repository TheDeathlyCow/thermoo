package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.command.v1.HeatingModeArgument;
import com.github.thedeathlycow.thermoo.api.command.v1.TemperatureUnitArgument;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.command.EnvironmentCommand;
import com.github.thedeathlycow.thermoo.impl.command.SoakingCommand;
import com.github.thedeathlycow.thermoo.impl.command.TemperatureCommand;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentModInitializer;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import com.github.thedeathlycow.thermoo.impl.core.UpdateEvents;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Thermoo implements ModInitializer {
    public static final String MODID = "thermoo";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final ArgumentTypeInfo<
            HeatingModeArgument,
            SingletonArgumentInfo<HeatingModeArgument>.Template
            > HEATING_MODE_ARG_SERIALIZER = SingletonArgumentInfo.contextFree(HeatingModeArgument::heatingMode);


    public static final ArgumentTypeInfo<
            TemperatureUnitArgument,
            SingletonArgumentInfo<TemperatureUnitArgument>.Template
            > TEMPERATURE_UNIT_ARG_SERIALIZER = SingletonArgumentInfo.contextFree(TemperatureUnitArgument::temperatureUnit);

    @Nullable
    private static ThermooConfig config = null;

    @Override
    public void onInitialize() {
        ArgumentTypeRegistry.registerArgumentType(
                Thermoo.id("heating_mode"),
                HeatingModeArgument.class,
                HEATING_MODE_ARG_SERIALIZER
        );

        ArgumentTypeRegistry.registerArgumentType(
                Thermoo.id("temperature_unit"),
                TemperatureUnitArgument.class,
                TEMPERATURE_UNIT_ARG_SERIALIZER
        );

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, context, selection) -> {
                    dispatcher.register(TemperatureCommand.create(dispatcher, context, selection));
                    dispatcher.register(EnvironmentCommand.create(dispatcher, context, selection));
                    dispatcher.register(SoakingCommand.create(dispatcher, context, selection));
                }
        );

        ServerLifecycleEvents.SERVER_STOPPED.register(TemperatureStatusManager::clearCaches);
        ServerLifecycleEvents.SERVER_STOPPED.register(UpdateEvents::clearCache);

        DynamicRegistries.register(
                ThermooRegistryKeys.ENVIRONMENT,
                EnvironmentDefinition.CODEC
        );
        DynamicRegistries.register(
                ThermooRegistryKeys.ENVIRONMENT_PROVIDER,
                EnvironmentProvider.ELEMENT_CODEC
        );
        DynamicRegistries.registerSynced(
                ThermooRegistryKeys.TEMPERATURE_STATUS,
                TemperatureStatus.DIRECT_CODEC
        );
        DynamicRegistries.register(
                ThermooRegistryKeys.TEMPERATURE_SOURCE,
                TemperatureSource.DIRECT_CODEC
        );

        ThermooCommonRegisters.registerTemperatureReductions();
        ThermooCommonRegisters.registerTemperatureEffects();
        ThermooCommonRegisters.registerEnvironmentProviderTypes();
        ThermooCommonRegisters.registerLootConditionTypes();
        ThermooCommonRegisters.registerEnvironmentAttributes();

        EnvironmentLookupImpl.initialize();

        initializeDependentEntryPoints();

        LOGGER.info("Thermoo initialized");
    }

    /**
     * Creates a new {@link Identifier} under the namespace {@value #MODID}
     *
     * @param path The identifier path
     * @return Returns a new {@link Identifier}
     */
    @Contract("_->new")
    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
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
