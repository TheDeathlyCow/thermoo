package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

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

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        var checkTemperature = literal("checktemperature")
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
                )
                .then(
                        argument("location", BlockPosArgumentType.blockPos())
                                .executes(
                                        context -> {
                                            return executeCheckTemperature(
                                                    context.getSource(),
                                                    BlockPosArgumentType.getBlockPos(
                                                            context,
                                                            "location"
                                                    )
                                            );
                                        }
                                )
                );

        dispatcher.register(
                literal("thermoo").then(
                        (literal("environment").requires((src) -> src.hasPermissionLevel(2)))
                                .then(checkTemperature)
                )
        );
    }

    private static int executeCheckTemperature(ServerCommandSource source, BlockPos location) {

        int temperatureChange = EnvironmentController.INSTANCE.getLocalTemperatureChange(
                source.getWorld(),
                location
        );


        var biome = source.getWorld().getBiome(location).getKey().orElse(null);

        Text msg = Text.translatable(
                "commands.thermoo.environment.checktemperature.success",
                location,
                biome == null ? "unknown" : biome.getValue(),
                temperatureChange
        );
        source.sendFeedback(msg, false);

        return temperatureChange;
    }
}
