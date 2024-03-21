package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.util.TemperatureConverter;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.FillCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SetBlockCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
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

        return literal("thermoo").then(
                (literal("environment").requires((src) -> src.hasPermissionLevel(2)))
                        .then(checkTemperature)
                        .then(printController)
        );
    }

    private static int printController(ServerCommandSource source) {
        String controller = EnvironmentManager.INSTANCE.getController().toString();

        source.sendFeedback(() -> Text.translatableWithFallback(
                "commands.thermoo.environment.printcontroller.success",
                "Controller logged to console"
        ), false);
        Thermoo.LOGGER.info("The current controller is: {}", controller);
        return 0;
    }

    private static int executeCheckTemperature(ServerCommandSource source, BlockPos location) {

        int temperatureChange = EnvironmentManager.INSTANCE.getController().getLocalTemperatureChange(
                source.getWorld(),
                location
        );


        var biome = source.getWorld().getBiome(location).getKey().orElse(null);

        source.sendFeedback(
                () -> Text.translatableWithFallback(
                        "commands.thermoo.environment.checktemperature.success",
                        "The passive temperature change at %s, %s, %s (%s) is %d",
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        biome == null ? "unknown" : biome.getValue(),
                        temperatureChange
                ),
                false
        );

        return temperatureChange;
    }

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
                        "The temperature at %s, %s, %s (%s) is %.2f°%s",
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        biome == null ? "unknown" : biome.getValue(),
                        temperature,
                        unit.getUnitSymbol()
                ),
                false
        );

        return (int) temperature;
    }
}
