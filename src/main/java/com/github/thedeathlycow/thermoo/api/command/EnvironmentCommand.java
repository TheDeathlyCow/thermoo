package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.util.TemperatureConverter;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.LivingEntityTickUtil;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import com.github.thedeathlycow.thermoo.impl.environment.ServerPlayerTickUtil;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;

import java.awt.*;
import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Command relating to environment effects
 * <p>
 * Usage:
 * <p>
 * {@code thermoo environment checktemperature <args>}
 * <p>
 * {@code thermoo environment printcontroller}
 * <p>
 * {@code thermoo environment temperature <pos> [<unit>] [<scale>]}
 * <p>
 * {@code thermoo environment relativehumidity <pos> [<scale>]}
 */
public class EnvironmentCommand {

    /**
     * Supplier for creating a new environment command builder to be registered to the Minecraft server
     * <p>
     * Registered by the default implementation of this API.
     */
    public static final Supplier<LiteralArgumentBuilder<ServerCommandSource>> COMMAND_BUILDER = EnvironmentCommand::buildCommand;

    @Contract("->new")
    private static LiteralArgumentBuilder<ServerCommandSource> buildCommand() {

        var printController = literal("printcontroller")
                .executes(
                        context -> {
                            return printController(context.getSource());
                        }
                );

        var checkTemperature = literal("checktemperature")
                .executes(
                        context -> {
                            var pos = context.getSource().getPosition();
                            return executeCheckTemperature(
                                    context.getSource(),
                                    new BlockPos((int) pos.x, (int) pos.y, (int) pos.z)
                            );
                        }
                )
                .then(
                        argument("target", EntityArgumentType.entity())
                                .executes(
                                        context -> {
                                            return executeCheckTemperature(
                                                    context.getSource(),
                                                    EntityArgumentType.getEntity(
                                                            context,
                                                            "target"
                                                    ).getBlockPos()
                                            );
                                        }
                                )
                                .then(
                                        argument("unit", TemperatureUnitArgumentType.temperatureUnit())
                                                .executes(
                                                        context -> {
                                                            return executeCheckTemperature(
                                                                    context.getSource(),
                                                                    EntityArgumentType.getEntity(
                                                                            context,
                                                                            "target"
                                                                    ).getBlockPos(),
                                                                    TemperatureUnitArgumentType.getTemperatureUnit(
                                                                            context,
                                                                            "unit"
                                                                    )
                                                            );
                                                        }
                                                )
                                )
                )
                .then(
                        argument("location", BlockPosArgumentType.blockPos())
                                .executes(
                                        context -> {
                                            return executeCheckTemperature(
                                                    context.getSource(),
                                                    BlockPosArgumentType.getLoadedBlockPos(
                                                            context,
                                                            "location"
                                                    )
                                            );
                                        }
                                )
                                .then(
                                        argument("unit", TemperatureUnitArgumentType.temperatureUnit())
                                                .executes(
                                                        context -> {
                                                            return executeCheckTemperature(
                                                                    context.getSource(),
                                                                    BlockPosArgumentType.getLoadedBlockPos(
                                                                            context,
                                                                            "location"
                                                                    ),
                                                                    TemperatureUnitArgumentType.getTemperatureUnit(
                                                                            context,
                                                                            "unit"
                                                                    )
                                                            );
                                                        }
                                                )
                                )
                );

        final String location = "location";
        final String target = "target";
        final String unit = "unit";
        final String scale = "scale";
        final TemperatureUnit fallbackUnit = TemperatureUnit.CELSIUS;
        final double fallbackTempScale = 1.0;

        var temperature = literal("temperature")
                .then(argument(target, EntityArgumentType.player())
                        .executes(
                                context -> executeEntityTemperature(
                                        context.getSource(),
                                        EntityArgumentType.getPlayer(context, target)
                                )
                        )
                )
                .then(argument(location, BlockPosArgumentType.blockPos())
                        .executes(
                                context -> executeTemperature(
                                        context.getSource(),
                                        BlockPosArgumentType.getLoadedBlockPos(context, location),
                                        TemperatureUnit.CELSIUS,
                                        fallbackTempScale
                                )
                        )
                        .then(
                                argument(unit, TemperatureUnitArgumentType.temperatureUnit())
                                        .executes(
                                                context -> executeTemperature(
                                                        context.getSource(),
                                                        BlockPosArgumentType.getLoadedBlockPos(context, location),
                                                        TemperatureUnitArgumentType.getTemperatureUnit(context, unit),
                                                        fallbackTempScale
                                                )
                                        )
                                        .then(
                                                argument(scale, DoubleArgumentType.doubleArg(0))
                                                        .executes(
                                                                context -> executeTemperature(
                                                                        context.getSource(),
                                                                        BlockPosArgumentType.getLoadedBlockPos(context, location),
                                                                        TemperatureUnitArgumentType.getTemperatureUnit(context, unit),
                                                                        DoubleArgumentType.getDouble(context, scale)
                                                                )
                                                        )
                                        )
                        )
                );

        final double fallbackHumidityScale = 100.0;

        var relativeHumidity = literal("relativehumidity").then(
                argument(location, BlockPosArgumentType.blockPos())
                        .executes(
                                context -> executeRelativeHumidity(
                                        context.getSource(),
                                        BlockPosArgumentType.getLoadedBlockPos(context, location),
                                        fallbackHumidityScale
                                )
                        )
                        .then(
                                argument(scale, DoubleArgumentType.doubleArg(0))
                                        .executes(
                                                context -> executeRelativeHumidity(
                                                        context.getSource(),
                                                        BlockPosArgumentType.getLoadedBlockPos(context, location),
                                                        DoubleArgumentType.getDouble(context, scale)
                                                )
                                        )
                        )
        );

        return literal("thermoo").then(
                (literal("environment").requires((src) -> src.hasPermissionLevel(2)))
                        .then(checkTemperature)
                        .then(printController)
                        .then(temperature)
                        .then(relativeHumidity)
        );
    }

    @Deprecated
    private static int printController(ServerCommandSource source) {
        String controller = EnvironmentManager.INSTANCE.getController().toString();

        source.sendFeedback(() -> Text.translatableWithFallback(
                "commands.thermoo.environment.printcontroller.success",
                "Controller logged to console"
        ), false);
        source.sendFeedback(
                () -> Text.translatableWithFallback(
                        "commands.thermoo.environment.printcontroller.deprecation",
                        "This command is deprecated, the Environment Controller has been replaced with the Environment Datapack Registry."
                ).formatted(Formatting.RED),
                false
        );

        Thermoo.LOGGER.info("The current controller is: {}", controller);
        return 0;
    }

    private static int executeEntityTemperature(ServerCommandSource source, ServerPlayerEntity target) {
        final ServerPlayerTickUtil.EnvironmentTickContextImpl context = ServerPlayerTickUtil.createContext(target);
        final var lookup = EnvironmentLookup.getInstance();
        context.components = lookup.findEnvironmentComponents(context.world(), context.pos());

        int tempChange = ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.invoker().addPointChange(context);
        double resistance = tempChange != 0
                ? tempChange > 0 ? target.thermoo$getEnvironmentHeatResistance() : target.thermoo$getEnvironmentColdResistance()
                : 0.0;

        source.sendFeedback(
                () -> Text.translatableWithFallback(
                        "commands.thermoo.environment.temperature.player.success",
                        "The environment temperature change of %s is %s (with a %s chance to dodge)",
                        target.getDisplayName(),
                        tempChange,
                        "%.2f%%".formatted(resistance * 100)
                ),
                false
        );

        return tempChange;
    }

    private static int executeTemperature(ServerCommandSource source, BlockPos location, TemperatureUnit unit, double scale) {
        double temperature = EnvironmentLookup.getInstance().findEnvironmentComponents(
                        source.getWorld(), location
                ).getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT)
                .temperature()
                .valueInUnit(unit);

        source.sendFeedback(
                () -> {
                    RegistryKey<Biome> biome = source.getWorld().getBiome(location).getKey().orElse(null);
                    return Text.translatableWithFallback(
                            "commands.thermoo.environment.temperature.success",
                            "The environment temperature at %s, %s, %s (%s) is %s°%s",
                            location.getX(),
                            location.getY(),
                            location.getZ(),
                            biome == null ? "unknown" : biome.getValue().toString(),
                            String.format("%.2f", temperature),
                            unit.getUnitSymbol()
                    );
                },
                false
        );

        return (int) (temperature * scale);
    }

    private static int executeRelativeHumidity(ServerCommandSource source, BlockPos location, double scale) {
        double relativeHumidity = EnvironmentLookup.getInstance().findEnvironmentComponents(
                source.getWorld(), location
        ).getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
        double scaledHumidity = relativeHumidity * scale;

        source.sendFeedback(
                () -> {
                    RegistryKey<Biome> biome = source.getWorld().getBiome(location).getKey().orElse(null);
                    return Text.translatableWithFallback(
                            "commands.thermoo.environment.humidity.success",
                            "The environmental relative humidity at %s, %s, %s (%s) is %s%",
                            location.getX(),
                            location.getY(),
                            location.getZ(),
                            biome == null ? "unknown" : biome.getValue().toString(),
                            String.format("%.2f", scaledHumidity)
                    );
                },
                false
        );

        return (int) (scaledHumidity);
    }

    @Deprecated
    private static int executeCheckTemperature(ServerCommandSource source, BlockPos location) {

        int temperatureChange = EnvironmentManager.INSTANCE.getController().getLocalTemperatureChange(
                source.getWorld(),
                location
        );


        var biome = source.getWorld().getBiome(location).getKey().orElse(null);

        source.sendFeedback(
                () -> Text.translatableWithFallback(
                        "commands.thermoo.environment.checktemperature.success",
                        "The passive temperature change at %s, %s, %s (%s) is %s",
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        biome == null ? "unknown" : biome.getValue().toString(),
                        temperatureChange
                ),
                false
        );
        source.sendFeedback(
                () -> Text.translatableWithFallback(
                        "commands.thermoo.environment.checktemperature.deprecation",
                        "This command is deprecated, use /thermoo environment temperature <pos>"
                ).formatted(Formatting.RED),
                false
        );

        return temperatureChange;
    }

    @Deprecated
    private static int executeCheckTemperature(ServerCommandSource source, BlockPos location, TemperatureUnit unit) {

        int temperatureTick = EnvironmentManager.INSTANCE.getController().getLocalTemperatureChange(
                source.getWorld(),
                location
        );


        var biome = source.getWorld().getBiome(location).getKey().orElse(null);

        double temperature = TemperatureConverter.temperatureTickToAmbientTemperature(
                temperatureTick,
                new TemperatureConverter.Settings(unit, 1, 0)
        );

        source.sendFeedback(
                () -> Text.translatableWithFallback(
                        "commands.thermoo.environment.checktemperature.unit.success",
                        "The temperature at %s, %s, %s (%s) is %s°%s",
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        biome == null ? "unknown" : biome.getValue().toString(),
                        String.format("%.2f", temperature),
                        unit.getUnitSymbol()
                ),
                false
        );
        source.sendFeedback(
                () -> Text.translatableWithFallback(
                        "commands.thermoo.environment.checktemperature.deprecation",
                        "This command is deprecated, use /thermoo environment temperature <pos>"
                ).formatted(Formatting.RED),
                false
        );

        return (int) temperature;
    }
}
