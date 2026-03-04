package com.github.thedeathlycow.thermoo.impl.command;

import com.github.thedeathlycow.thermoo.api.command.v1.TemperatureUnitArgument;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.component.AtmosphericPressureComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.LivingEntityTickUtil;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentTickContextImpl;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
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
public final class EnvironmentCommand {
    private EnvironmentCommand() {

    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext buildContext,
            Commands.CommandSelection selection
    ) {
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
                                        fallbackUnit,
                                        fallbackTempScale
                                )
                        )
                        .then(
                                argument(unit, TemperatureUnitArgument.temperatureUnit())
                                        .executes(
                                                context -> executeTemperature(
                                                        context.getSource(),
                                                        BlockPosArgument.getLoadedBlockPos(context, location),
                                                        TemperatureUnitArgument.getTemperatureUnit(context, unit),
                                                        fallbackTempScale
                                                )
                                        )
                                        .then(
                                                argument(scale, DoubleArgumentType.doubleArg(0))
                                                        .executes(
                                                                context -> executeTemperature(
                                                                        context.getSource(),
                                                                        BlockPosArgument.getLoadedBlockPos(context, location),
                                                                        TemperatureUnitArgument.getTemperatureUnit(context, unit),
                                                                        DoubleArgumentType.getDouble(context, scale)
                                                                )
                                                        )
                                        )
                        )
                );

        final double fallbackHumidityScale = 100.0;

        var relativeHumidityArg = argument(location, BlockPosArgument.blockPos())
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
                );

        var relativeHumidityOld = literal("relativehumidity").then(relativeHumidityArg);
        var relativeHumidity = literal("relative_humidity").then(relativeHumidityArg);

        final double fallbackPressureScale = 1.0;

        var pressure = literal("atmospheric_pressure").then(
                argument(location, BlockPosArgument.blockPos())
                        .executes(
                                context -> executeAtmosphericPressure(
                                        context.getSource(),
                                        BlockPosArgument.getLoadedBlockPos(context, location),
                                        fallbackPressureScale
                                )
                        )
                        .then(
                                argument(scale, DoubleArgumentType.doubleArg(0))
                                        .executes(
                                                context -> executeAtmosphericPressure(
                                                        context.getSource(),
                                                        BlockPosArgument.getLoadedBlockPos(context, location),
                                                        DoubleArgumentType.getDouble(context, scale)
                                                )
                                        )
                        )
        );

        return literal("thermoo").then(
                (literal("environment").requires((src) -> src.permissions()
                        .hasPermission(Permissions.COMMANDS_GAMEMASTER)))
                        .then(temperature)
                        .then(relativeHumidity)
                        .then(relativeHumidityOld)
                        .then(pressure)
        );
    }

    private static int executeEntityTemperature(CommandSourceStack source, ServerPlayer target) {
        BlockPos pos = LivingEntityTickUtil.getTemperatureTickPos(target);
        ServerLevel world = target.level();
        final EnvironmentTickContextImpl<ServerPlayer> context = new EnvironmentTickContextImpl<>(
                target,
                world,
                pos,
                EnvironmentLookup.getInstance().findEnvironmentComponents(world, pos)
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
                            biome == null ? "unknown" : biome.identifier().toString(),
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
                            biome == null ? "unknown" : biome.identifier().toString(),
                            String.format("%.2f", scaledHumidity)
                    );
                },
                false
        );

        return (int) (scaledHumidity);
    }

    private static int executeAtmosphericPressure(CommandSourceStack source, BlockPos location, double scale) {
        double atmosphericPressure = EnvironmentLookup.getInstance().findEnvironmentComponents(
                source.getLevel(), location
        ).getOrDefault(EnvironmentComponentTypes.ATMOSPHERIC_PRESSURE, AtmosphericPressureComponent.DEFAULT);
        double scaledAtmosphericPressure = atmosphericPressure * scale;

        source.sendSuccess(
                () -> {
                    ResourceKey<Biome> biome = source.getLevel().getBiome(location).unwrapKey().orElse(null);
                    return Component.translatable(
                            "commands.thermoo.environment.atmospheric_pressure.success",
                            location.getX(),
                            location.getY(),
                            location.getZ(),
                            biome == null ? "unknown" : biome.identifier().toString(),
                            String.format("%.2f", scaledAtmosphericPressure)
                    );
                },
                false
        );

        return (int) (scaledAtmosphericPressure);
    }
}
