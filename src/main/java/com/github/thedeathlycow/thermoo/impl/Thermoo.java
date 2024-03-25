package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.attribute.ItemAttributeModifier;
import com.github.thedeathlycow.thermoo.api.command.EnvironmentCommand;
import com.github.thedeathlycow.thermoo.api.command.HeatingModeArgumentType;
import com.github.thedeathlycow.thermoo.api.command.TemperatureCommand;
import com.github.thedeathlycow.thermoo.api.command.TemperatureUnitArgumentType;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasonEvents;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasons;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import io.github.lucaargolo.seasons.FabricSeasons;
import io.github.lucaargolo.seasons.utils.Season;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

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
        ArgumentTypeRegistry.registerArgumentType(
                Thermoo.id("temperature_unit"),
                TemperatureUnitArgumentType.class,
                ConstantArgumentSerializer.of(TemperatureUnitArgumentType::temperatureUnit)
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
        registerFabricSeasonsIntegration();
        LOGGER.info("Creating environment manager {}", EnvironmentManager.INSTANCE);
        LOGGER.info("Thermoo initialized");
    }

    private static void registerFabricSeasonsIntegration() {
        if (FabricLoader.getInstance().isModLoaded(ThermooIntegrations.FABRIC_SEASONS_ID)) {
            LOGGER.warn("Registering builtin Fabric Seasons integration with Thermoo. " +
                    "Note that this integration will be removed as a builtin feature in the future.");
            ThermooSeasonEvents.GET_CURRENT_SEASON.register(world -> {
                Season fabricSeason = FabricSeasons.getCurrentSeason(world);
                return Optional.ofNullable(
                        switch (fabricSeason) {
                            case WINTER -> ThermooSeasons.WINTER;
                            case SUMMER -> ThermooSeasons.SUMMER;
                            case FALL -> ThermooSeasons.AUTUMN;
                            case SPRING -> ThermooSeasons.SPRING;
                            default -> null;
                        }
                );
            });
        }
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
