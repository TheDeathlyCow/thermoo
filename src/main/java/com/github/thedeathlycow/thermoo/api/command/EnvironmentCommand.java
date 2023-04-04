package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SetBlockCommand;
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

        var checkTemperature = literal("checktemperature")
                .executes(
                        context -> {
                            return executeCheckTemperature(
                                    context.getSource(),
                                    new BlockPos(context.getSource().getPosition())
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

        return literal("thermoo").then(
                (literal("environment").requires((src) -> src.hasPermissionLevel(2)))
                        .then(checkTemperature)
        );
    }

    private static int executeCheckTemperature(ServerCommandSource source, BlockPos location) {

        int temperatureChange = EnvironmentManager.INSTANCE.getController().getLocalTemperatureChange(
                source.getWorld(),
                location
        );


        var biome = source.getWorld().getBiome(location).getKey().orElse(null);

        Text msg = Text.translatable(
                "commands.thermoo.environment.checktemperature.success",
                location.getX(),
                location.getY(),
                location.getZ(),
                biome == null ? "unknown" : biome.getValue(),
                temperatureChange
        );
        source.sendFeedback(msg, false);

        return temperatureChange;
    }
}
