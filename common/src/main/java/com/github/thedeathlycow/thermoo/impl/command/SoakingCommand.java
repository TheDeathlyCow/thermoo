package com.github.thedeathlycow.thermoo.impl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * Command relating to soaking. Allows soaking values to be modified in game.
 * <p>
 * Usage:
 * <p>
 * {@code thermoo soaking (get|set|add|remove) <target> <args>}
 */
public final class SoakingCommand {
    private static final String TARGET_KEY = "target";
    private static final String SCALE_KEY = "scale";
    private static final String MIN_KEY = "min";
    private static final String MAX_KEY = "max";
    private static final String VALUE_KEY = "value";

    public static LiteralArgumentBuilder<CommandSourceStack> create(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext buildContext,
            Commands.CommandSelection selection
    ) {
        return literal("thermoo").then(
                (literal("soaking").requires(src -> src.permissions()
                        .hasPermission(Permissions.COMMANDS_GAMEMASTER)))
                        .then(buildGetCommand())
                        .then(buildSetCommand())
                        .then(buildAddCommand())
                        .then(buildRemoveCommand())
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildRemoveCommand() {
        return literal("remove")
                .then(
                        argument(TARGET_KEY, EntityArgument.entity())
                                .then(
                                        argument(VALUE_KEY, IntegerArgumentType.integer(0))
                                                .executes(
                                                        context -> {
                                                            return remove(
                                                                    context.getSource(),
                                                                    EntityArgument.getEntity(context, TARGET_KEY),
                                                                    IntegerArgumentType.getInteger(context, VALUE_KEY)
                                                            );
                                                        }
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildAddCommand() {
        return literal("add")
                .then(
                        argument(TARGET_KEY, EntityArgument.entity())
                                .then(
                                        argument(VALUE_KEY, IntegerArgumentType.integer(0))
                                                .executes(
                                                        context -> {
                                                            return add(
                                                                    context.getSource(),
                                                                    EntityArgument.getEntity(context, TARGET_KEY),
                                                                    IntegerArgumentType.getInteger(context, VALUE_KEY)
                                                            );
                                                        }
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildSetCommand() {
        return literal("set")
                .then(
                        argument(TARGET_KEY, EntityArgument.entity())
                                .then(
                                        argument(VALUE_KEY, IntegerArgumentType.integer(0))
                                                .executes(
                                                        context -> {
                                                            return set(
                                                                    context.getSource(),
                                                                    EntityArgument.getEntity(context, TARGET_KEY),
                                                                    IntegerArgumentType.getInteger(context, VALUE_KEY)
                                                            );
                                                        }
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildGetCommand() {
        Command<CommandSourceStack> getCurrent = context -> {
            return getCurrent(
                    context.getSource(),
                    EntityArgument.getEntity(context, TARGET_KEY)
            );
        };

        var getScale = literal(SCALE_KEY)
                .then(
                        argument(SCALE_KEY, IntegerArgumentType.integer(1))
                                .executes(
                                        context -> {
                                            return getScale(
                                                    context.getSource(),
                                                    EntityArgument.getEntity(context, TARGET_KEY),
                                                    IntegerArgumentType.getInteger(context, SCALE_KEY)
                                            );
                                        }
                                )
                )
                .executes(
                        context -> {
                            return getScale(
                                    context.getSource(),
                                    EntityArgument.getEntity(context, TARGET_KEY),
                                    100
                            );
                        }
                );

        var getMin = literal(MIN_KEY)
                .executes(
                        context -> {
                            return getMin(
                                    context.getSource(),
                                    EntityArgument.getEntity(context, TARGET_KEY)
                            );
                        }
                );

        var getMax = literal(MAX_KEY)
                .executes(
                        context -> {
                            return getMax(
                                    context.getSource(),
                                    EntityArgument.getEntity(context, TARGET_KEY)
                            );
                        }
                );

        return literal("get")
                .then(
                        argument(TARGET_KEY, EntityArgument.entity())
                                .executes(getCurrent)
                                .then(literal("current").executes(getCurrent))
                                .then(getScale)
                                .then(getMin)
                                .then(getMax)
                );
    }

    private static int remove(CommandSourceStack source, Entity target, int value) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            entity.thermoo$addWetTicks(-value);

            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.soaking.remove.success",
                            target.getDisplayName(),
                            value,
                            entity.thermoo$getWetTicks()
                    ), true
            );

            return entity.thermoo$getWetTicks();
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int add(CommandSourceStack source, Entity target, int value) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            entity.thermoo$addWetTicks(value);

            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.soaking.add.success",
                            target.getDisplayName(),
                            value,
                            entity.thermoo$getWetTicks()
                    ), true
            );

            return entity.thermoo$getWetTicks();
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int set(CommandSourceStack source, Entity target, int value) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            entity.thermoo$setWetTicks(value);

            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.soaking.set.success",
                            target.getDisplayName(),
                            value,
                            entity.thermoo$getWetTicks()
                    ), true
            );

            return entity.thermoo$getWetTicks();
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int getMax(CommandSourceStack source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            int value = entity.thermoo$getMaxWetTicks();

            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.soaking.get.max.success",
                            target.getDisplayName(),
                            value
                    ), false
            );

            return value;
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int getMin(CommandSourceStack source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity) {
            int value = 0;

            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.soaking.get.min.success",
                            target.getDisplayName(),
                            value
                    ), false
            );

            return value;
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int getScale(CommandSourceStack source, Entity target, int scale) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            int value = Mth.floor(entity.thermoo$getSoakedScale() * scale);

            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.soaking.get.scale.success",
                            target.getDisplayName(),
                            value
                    ), false
            );

            return value;
        } else {
            throw TemperatureCommand.NOT_LIVING_ENTITY.create();
        }
    }

    private static int getCurrent(CommandSourceStack source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity entity) {
            int value = entity.thermoo$getWetTicks();

            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.soaking.get.current.success",
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
