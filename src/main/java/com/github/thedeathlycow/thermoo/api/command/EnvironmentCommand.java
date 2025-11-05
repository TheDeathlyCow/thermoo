package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.util.TemperatureConverter;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.LivingEntityTickUtil;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentTickContextImpl;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

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
    public static final Supplier<LiteralArgumentBuilder<CommandSourceStack>> COMMAND_BUILDER = EnvironmentCommand::buildCommand;

    @Contract("->new")
    private static LiteralArgumentBuilder<CommandSourceStack> buildCommand() {

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
                        argument("target", EntityArgument.entity())
                                .executes(
                                        context -> {
                                            return executeCheckTemperature(
                                                    context.getSource(),
                                                    EntityArgument.getEntity(
                                                            context,
                                                            "target"
                                                    ).blockPosition()
                                            );
                                        }
                                )
                                .then(
                                        argument("unit", TemperatureUnitArgumentType.temperatureUnit())
                                                .executes(
                                                        context -> {
                                                            return executeCheckTemperature(
                                                                    context.getSource(),
                                                                    EntityArgument.getEntity(
                                                                            context,
                                                                            "target"
                                                                    ).blockPosition(),
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
                        argument("location", BlockPosArgument.blockPos())
                                .executes(
                                        context -> {
                                            return executeCheckTemperature(
                                                    context.getSource(),
                                                    BlockPosArgument.getLoadedBlockPos(
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
                                                                    BlockPosArgument.getLoadedBlockPos(
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
                .then(argument(target, EntityArgument.player())
                        .executes(
                                context -> executeEntityTemperature(
                                        context.getSource(),
                                        EntityArgument.getPlayer(context, target)
                                )
                        )
                )
                .then(argument(location, BlockPosArgument.blockPos())
                        .executes(
                                context -> executeTemperature(
                                        context.getSource(),
                                        BlockPosArgument.getLoadedBlockPos(context, location),
                                        TemperatureUnit.CELSIUS,
                                        fallbackTempScale
                                )
                        )
                        .then(
                                argument(unit, TemperatureUnitArgumentType.temperatureUnit())
                                        .executes(
                                                context -> executeTemperature(
                                                        context.getSource(),
                                                        BlockPosArgument.getLoadedBlockPos(context, location),
                                                        TemperatureUnitArgumentType.getTemperatureUnit(context, unit),
                                                        fallbackTempScale
                                                )
                                        )
                                        .then(
                                                argument(scale, DoubleArgumentType.doubleArg(0))
                                                        .executes(
                                                                context -> executeTemperature(
                                                                        context.getSource(),
                                                                        BlockPosArgument.getLoadedBlockPos(context, location),
                                                                        TemperatureUnitArgumentType.getTemperatureUnit(context, unit),
                                                                        DoubleArgumentType.getDouble(context, scale)
                                                                )
                                                        )
                                        )
                        )
                );

        final double fallbackHumidityScale = 100.0;

        var relativeHumidity = literal("relativehumidity").then(
                argument(location, BlockPosArgument.blockPos())
                        .executes(
                                context -> executeRelativeHumidity(
                                        context.getSource(),
                                        BlockPosArgument.getLoadedBlockPos(context, location),
                                        fallbackHumidityScale
                                )
                        )
                        .then(
                                argument(scale, DoubleArgumentType.doubleArg(0))
                                        .executes(
                                                context -> executeRelativeHumidity(
                                                        context.getSource(),
                                                        BlockPosArgument.getLoadedBlockPos(context, location),
                                                        DoubleArgumentType.getDouble(context, scale)
                                                )
                                        )
                        )
        );

        return literal("thermoo").then(
                (literal("environment").requires((src) -> src.hasPermission(2)))
                        .then(checkTemperature)
                        .then(printController)
                        .then(temperature)
                        .then(relativeHumidity)
        );
    }

    @Deprecated
    private static int printController(CommandSourceStack source) {
        String controller = EnvironmentManager.INSTANCE.getController().toString();

        source.sendSuccess(() -> Component.translatableWithFallback(
                "commands.thermoo.environment.printcontroller.success",
                "Controller logged to console"
        ), false);
        source.sendSuccess(
                () -> Component.translatableWithFallback(
                        "commands.thermoo.environment.printcontroller.deprecation",
                        "This command is deprecated, the Environment Controller has been replaced with the Environment Datapack Registry."
                ).withStyle(ChatFormatting.RED),
                false
        );

        Thermoo.LOGGER.info("The current controller is: {}", controller);
        return 0;
    }

    private static int executeEntityTemperature(CommandSourceStack source, ServerPlayer target) {
        BlockPos pos = LivingEntityTickUtil.getTemperatureTickPos(target);
        final EnvironmentTickContextImpl<ServerPlayer> context = new EnvironmentTickContextImpl<>(
                target,
                target.serverLevel(),
                pos,
                EnvironmentLookup.getInstance().findEnvironmentComponents(target.serverLevel(), pos)
        );

        int tempChange = ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.invoker().addPointChange(context);
        double resistance = tempChange != 0
                ? tempChange > 0 ? target.thermoo$getEnvironmentHeatResistance() : target.thermoo$getEnvironmentColdResistance()
                : 0.0;

        if (resistance >= 0) {
            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.environment.temperature.player.success",
                            target.getDisplayName(),
                            tempChange,
                            "%.2f%%".formatted(resistance * 100)
                    ),
                    false
            );
        } else {
            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.environment.temperature.player.negative.success",
                            target.getDisplayName(),
                            tempChange,
                            "%.2f%%".formatted(resistance * -100)
                    ),
                    false
            );

        }

        return tempChange;
    }

    private static int executeTemperature(CommandSourceStack source, BlockPos location, TemperatureUnit unit, double scale) {
        double temperature = EnvironmentLookup.getInstance().findEnvironmentComponents(
                        source.getLevel(), location
                ).getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT)
                .valueInUnit(unit);

        source.sendSuccess(
                () -> {
                    ResourceKey<Biome> biome = source.getLevel().getBiome(location).unwrapKey().orElse(null);
                    return Component.translatable(
                            "commands.thermoo.environment.temperature.success",
                            location.getX(),
                            location.getY(),
                            location.getZ(),
                            biome == null ? "unknown" : biome.location().toString(),
                            String.format("%.2f", temperature),
                            unit.getUnitSymbol()
                    );
                },
                false
        );

        return (int) (temperature * scale);
    }

    private static int executeRelativeHumidity(CommandSourceStack source, BlockPos location, double scale) {
        double relativeHumidity = EnvironmentLookup.getInstance().findEnvironmentComponents(
                source.getLevel(), location
        ).getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
        double scaledHumidity = relativeHumidity * scale;

        source.sendSuccess(
                () -> {
                    ResourceKey<Biome> biome = source.getLevel().getBiome(location).unwrapKey().orElse(null);
                    return Component.translatable(
                            "commands.thermoo.environment.humidity.success",
                            location.getX(),
                            location.getY(),
                            location.getZ(),
                            biome == null ? "unknown" : biome.location().toString(),
                            String.format("%.2f", scaledHumidity)
                    );
                },
                false
        );

        return (int) (scaledHumidity);
    }

    @Deprecated
    private static int executeCheckTemperature(CommandSourceStack source, BlockPos location) {

        int temperatureChange = EnvironmentManager.INSTANCE.getController().getLocalTemperatureChange(
                source.getLevel(),
                location
        );


        var biome = source.getLevel().getBiome(location).unwrapKey().orElse(null);

        source.sendSuccess(
                () -> Component.translatableWithFallback(
                        "commands.thermoo.environment.checktemperature.success",
                        "The passive temperature change at %s, %s, %s (%s) is %s",
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        biome == null ? "unknown" : biome.location().toString(),
                        temperatureChange
                ),
                false
        );
        source.sendSuccess(
                () -> Component.translatableWithFallback(
                        "commands.thermoo.environment.checktemperature.deprecation",
                        "This command is deprecated, use /thermoo environment temperature <pos>"
                ).withStyle(ChatFormatting.RED),
                false
        );

        return temperatureChange;
    }

    @Deprecated
    private static int executeCheckTemperature(CommandSourceStack source, BlockPos location, TemperatureUnit unit) {

        int temperatureTick = EnvironmentManager.INSTANCE.getController().getLocalTemperatureChange(
                source.getLevel(),
                location
        );


        var biome = source.getLevel().getBiome(location).unwrapKey().orElse(null);

        double temperature = TemperatureConverter.temperatureTickToAmbientTemperature(
                temperatureTick,
                new TemperatureConverter.Settings(unit, 1, 0)
        );

        source.sendSuccess(
                () -> Component.translatableWithFallback(
                        "commands.thermoo.environment.checktemperature.unit.success",
                        "The temperature at %s, %s, %s (%s) is %s°%s",
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        biome == null ? "unknown" : biome.location().toString(),
                        String.format("%.2f", temperature),
                        unit.getUnitSymbol()
                ),
                false
        );
        source.sendSuccess(
                () -> Component.translatableWithFallback(
                        "commands.thermoo.environment.checktemperature.deprecation",
                        "This command is deprecated, use /thermoo environment temperature <pos>"
                ).withStyle(ChatFormatting.RED),
                false
        );

        return (int) temperature;
    }
}
