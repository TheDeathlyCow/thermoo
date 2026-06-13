package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.command.v1.TemperatureUnitArgument;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.command.ThermooArgumentTypeRegistry;
import com.github.thedeathlycow.thermoo.impl.command.EnvironmentCommand;
import com.github.thedeathlycow.thermoo.impl.command.SoakingCommand;
import com.github.thedeathlycow.thermoo.impl.command.TemperatureCommand;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooCommandRegistrationCallback;
import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooServerLifecycleEvents;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusManager;
import dev.yumi.commons.event.EventManager;
import dev.yumi.mc.core.api.ModContainer;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Thermoo {
    public static final String MODID = "thermoo";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final EventManager<Identifier> EVENT_MANAGER = new EventManager<>(id("default"), Identifier::parse);

    public static final EventManager<Identifier> IMPL_EVENT_MANAGER = new EventManager<>(id("default"), Identifier::parse);

    public static final ArgumentTypeInfo<
            TemperatureUnitArgument,
            SingletonArgumentInfo<TemperatureUnitArgument>.Template
            > TEMPERATURE_UNIT_ARG_SERIALIZER = SingletonArgumentInfo.contextFree(TemperatureUnitArgument::temperatureUnit);

    @Nullable
    private static ThermooConfig config = null;

    public static void onInitialize(ModContainer mod) {
        ThermooCommonRegisters.registerTemperatureReductions();
        ThermooCommonRegisters.registerTemperatureEffects();
        ThermooCommonRegisters.registerEnvironmentProviderTypes();
        ThermooCommonRegisters.registerLootConditionTypes();
        ThermooCommonRegisters.registerEnvironmentAttributes();

        ThermooArgumentTypeRegistry.registerArgumentType(
                Thermoo.id("temperature_unit"),
                TemperatureUnitArgument.class,
                TEMPERATURE_UNIT_ARG_SERIALIZER
        );

        EnvironmentLookupImpl.initialize();

        ThermooServerLifecycleEvents.SERVER_STOPPED.register(TemperatureStatusManager::clearCaches);

        ThermooServices.REGISTRIES.register(
                ThermooRegistries.ENVIRONMENT,
                EnvironmentDefinition.CODEC
        );
        ThermooServices.REGISTRIES.register(
                ThermooRegistries.ENVIRONMENT_PROVIDER,
                EnvironmentProvider.ELEMENT_CODEC
        );
        ThermooServices.REGISTRIES.registerSynced(
                ThermooRegistries.TEMPERATURE_STATUS,
                TemperatureStatus.DIRECT_CODEC
        );
        ThermooServices.REGISTRIES.registerSynced(
                ThermooRegistries.TEMPERATURE_SOURCE,
                TemperatureSource.DIRECT_CODEC
        );

        ThermooCommandRegistrationCallback.EVENT.register(
                (dispatcher, context, selection) -> {
                    dispatcher.register(TemperatureCommand.create(dispatcher, context, selection));
                    dispatcher.register(EnvironmentCommand.create(dispatcher, context, selection));
                    dispatcher.register(SoakingCommand.create(dispatcher, context, selection));
                }
        );

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

    private Thermoo() {

    }
}
