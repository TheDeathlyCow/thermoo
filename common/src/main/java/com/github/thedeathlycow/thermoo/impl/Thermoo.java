package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.command.v1.TemperatureUnitArgument;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
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

        EnvironmentLookupImpl.initialize();

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
