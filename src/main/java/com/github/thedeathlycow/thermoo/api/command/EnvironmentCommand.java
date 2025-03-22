package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.LivingEntityTickUtil;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentTickContextImpl;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;

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
public final class EnvironmentCommand {
    private EnvironmentCommand() {

    }

    /**
     * Supplier for creating a new environment command builder to be registered to the Minecraft server
     * <p>
     * Registered by the default implementation of this API.
     */
    public static final Supplier<LiteralArgumentBuilder<ServerCommandSource>> COMMAND_BUILDER = EnvironmentCommand::buildCommand;

    @Contract("->new")
    private static LiteralArgumentBuilder<ServerCommandSource> buildCommand() {
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
                        .then(temperature)
                        .then(relativeHumidity)
        );
    }

    private static int executeEntityTemperature(ServerCommandSource source, ServerPlayerEntity target) {
        BlockPos pos = LivingEntityTickUtil.getTemperatureTickPos(target);
        final EnvironmentTickContextImpl<ServerPlayerEntity> context = new EnvironmentTickContextImpl<>(
                target,
                target.getServerWorld(),
                pos,
                EnvironmentLookup.getInstance().findEnvironmentComponents(target.getServerWorld(), pos)
        );

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
}
