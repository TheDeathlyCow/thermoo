package com.github.thedeathlycow.thermoo.api.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class SoakingCommand {

    public static final Supplier<LiteralArgumentBuilder<ServerCommandSource>> COMMAND_BUILDER = SoakingCommand::buildCommand;

    private static final String TARGET_KEY = "target";
    private static final String SCALE_KEY = "scale";
    private static final String MIN_KEY = "min";
    private static final String MAX_KEY = "max";
    private static final String VALUE_KEY = "value";

    @Contract("->new")
    private static LiteralArgumentBuilder<ServerCommandSource> buildCommand() {
        return literal("thermoo").then(
                (literal("soaking").requires(src -> src.hasPermissionLevel(2)))
                        .then(buildGetCommand())
                        .then(buildSetCommand())
        );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> buildSetCommand() {
        return literal("set")
                .then(
                        argument(TARGET_KEY, EntityArgumentType.entity())
                                .then(
                                        argument(VALUE_KEY, IntegerArgumentType.integer(0))
                                                .executes(
                                                        context -> {
                                                            return set(
                                                                    context.getSource(),
                                                                    EntityArgumentType.getEntity(context, TARGET_KEY),
                                                                    IntegerArgumentType.getInteger(context, VALUE_KEY)
                                                            );
                                                        }
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> buildGetCommand() {

        Command<ServerCommandSource> getCurrent = context -> {
            return getCurrent(
                    context.getSource(),
                    EntityArgumentType.getEntity(context, TARGET_KEY)
            );
        };

        var getScale = literal(SCALE_KEY)
                .then(
                        argument(SCALE_KEY, IntegerArgumentType.integer(1))
                                .executes(
                                        context -> {
                                            return getScale(
                                                    context.getSource(),
                                                    EntityArgumentType.getEntity(context, TARGET_KEY),
                                                    IntegerArgumentType.getInteger(context, SCALE_KEY)
                                            );
                                        }
                                )
                )
                .executes(
                        context -> {
                            return getScale(
                                    context.getSource(),
                                    EntityArgumentType.getEntity(context, TARGET_KEY),
                                    100
                            );
                        }
                );

        var getMin = literal(MIN_KEY)
                .executes(
                        context -> {
                            return getMin(
                                    context.getSource(),
                                    EntityArgumentType.getEntity(context, TARGET_KEY)
                            );
                        }
                );

        var getMax = literal(MAX_KEY)
                .executes(
                        context -> {
                            return getMax(
                                    context.getSource(),
                                    EntityArgumentType.getEntity(context, TARGET_KEY)
                            );
                        }
                );

        return literal("get")
                .then(
                        argument(TARGET_KEY, EntityArgumentType.entity())
                                .executes(getCurrent)
                                .then(literal("current").executes(getCurrent))
                                .then(getScale)
                                .then(getMin)
                                .then(getMax)
                );
    }

    private static int set(ServerCommandSource source, Entity target, int value) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            entity.thermoo$setWetTicks(value);

            source.sendFeedback(
                    () -> Text.translatableWithFallback(
                            "commands.thermoo.soaking.set.success",
                            "Set the soaking value of %s to %d (now %d)",
                            target.getDisplayName(),
                            value,
                            entity.thermoo$getWetTicks()
                    ), false
            );

            return entity.thermoo$getWetTicks();
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int getMax(ServerCommandSource source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            int value = entity.thermoo$getMaxWetTicks();

            source.sendFeedback(
                    () -> Text.translatableWithFallback(
                            "commands.thermoo.soaking.get.max.success",
                            "The maximum soaking value of %s is %d",
                            target.getDisplayName(),
                            value
                    ), false
            );

            return value;
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int getMin(ServerCommandSource source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity) {
            int value = 0;

            source.sendFeedback(
                    () -> Text.translatableWithFallback(
                            "commands.thermoo.soaking.get.min.success",
                            "The minimum soaking value of %s is %d",
                            target.getDisplayName(),
                            value
                    ), false
            );

            return value;
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int getScale(ServerCommandSource source, Entity target, int scale) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            int value = MathHelper.floor(entity.thermoo$getSoakedScale() * scale);

            source.sendFeedback(
                    () -> Text.translatableWithFallback(
                            "commands.thermoo.soaking.get.scale.success",
                            "The current soaking scale of %s is %d",
                            target.getDisplayName(),
                            value
                    ), false
            );

            return value;
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int getCurrent(ServerCommandSource source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            int value = entity.thermoo$getWetTicks();

            source.sendFeedback(
                    () -> Text.translatableWithFallback(
                            "commands.thermoo.soaking.get.current.success",
                            "The current soaking value of %s is %d",
                            target.getDisplayName(),
                            value
                    ), false
            );

            return value;
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private SoakingCommand() {

    }
}
