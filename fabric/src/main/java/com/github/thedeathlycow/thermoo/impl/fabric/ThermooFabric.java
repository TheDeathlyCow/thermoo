package com.github.thedeathlycow.thermoo.impl.fabric;

import com.github.thedeathlycow.thermoo.api.command.v1.TemperatureUnitArgument;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.command.EnvironmentCommand;
import com.github.thedeathlycow.thermoo.impl.command.SoakingCommand;
import com.github.thedeathlycow.thermoo.impl.command.TemperatureCommand;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentModInitializer;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusManager;
import dev.yumi.mc.core.api.ModContainer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Arrays;
import java.util.List;

import dev.yumi.mc.core.api.entrypoint.ModInitializer;

public class ThermooFabric implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        Thermoo.onInitialize(mod);

        ArgumentTypeRegistry.registerArgumentType(
                Thermoo.id("temperature_unit"),
                TemperatureUnitArgument.class,
                Thermoo.TEMPERATURE_UNIT_ARG_SERIALIZER
        );

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, context, selection) -> {
                    dispatcher.register(TemperatureCommand.create(dispatcher, context, selection));
                    dispatcher.register(EnvironmentCommand.create(dispatcher, context, selection));
                    dispatcher.register(SoakingCommand.create(dispatcher, context, selection));
                }
        );

        ServerLifecycleEvents.SERVER_STOPPED.register(TemperatureStatusManager::clearCaches);

        DynamicRegistries.register(
                ThermooRegistries.ENVIRONMENT,
                EnvironmentDefinition.CODEC
        );
        DynamicRegistries.register(
                ThermooRegistries.ENVIRONMENT_PROVIDER,
                EnvironmentProvider.ELEMENT_CODEC
        );
        DynamicRegistries.registerSynced(
                ThermooRegistries.TEMPERATURE_STATUS,
                TemperatureStatus.DIRECT_CODEC
        );
        DynamicRegistries.registerSynced(
                ThermooRegistries.TEMPERATURE_SOURCE,
                TemperatureSource.DIRECT_CODEC
        );

        initializeDependentEntryPoints();
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