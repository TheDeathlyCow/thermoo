package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * Command relating to temperature. Allows temperature to be modified in game.
 * <p>
 * Usage:
 * <p>
 * {@code thermoo temperature <subcommand> <args>}
 */
public class TemperatureCommand {

    static final SimpleCommandExceptionType NOT_LIVING_ENTITY = new SimpleCommandExceptionType(
            Component.translatable("commands.thermoo.temperature.exception.not_living_entity")
    );

    /**
     * Supplier for creating a new temperature command builder to be registered to the Minecraft server
     * <p>
     * Registered by the default implementation of this API.
     */
    public static final Supplier<LiteralArgumentBuilder<CommandSourceStack>> COMMAND_BUILDER = TemperatureCommand::buildCommand;

    @Contract("->new")
    private static LiteralArgumentBuilder<CommandSourceStack> buildCommand() {
        var getSubCommand = literal("get")
                .then(
                        argument("target", EntityArgument.entity())
                                .executes(context -> {
                                    return runGetCurrent(
                                            context.getSource(),
                                            EntityArgument.getEntity(context, "target")
                                    );
                                })
                                .then(literal("current")
                                        .executes(context -> {
                                            return runGetCurrent(
                                                    context.getSource(),
                                                    EntityArgument.getEntity(context, "target")
                                            );
                                        })
                                )
                                .then(literal("max")
                                        .executes(context -> {
                                            return runGetMax(
                                                    context.getSource(),
                                                    EntityArgument.getEntity(context, "target")
                                            );
                                        })
                                )
                                .then(literal("min")
                                        .executes(context -> {
                                            return runGetMin(
                                                    context.getSource(),
                                                    EntityArgument.getEntity(context, "target")
                                            );
                                        })
                                )
                                .then(literal("scale")
                                        .executes(context -> {
                                            return runGetScale(
                                                    context.getSource(),
                                                    EntityArgument.getEntity(context, "target"),
                                                    100
                                            );
                                        })
                                        .then(argument("scale", IntegerArgumentType.integer(1))
                                                .executes(context -> {
                                                    return runGetScale(
                                                            context.getSource(),
                                                            EntityArgument.getEntity(context, "target"),
                                                            IntegerArgumentType.getInteger(context, "scale")
                                                    );
                                                })
                                        )
                                )

                );


        var remove = literal("remove")
                .then(
                        argument("targets", EntityArgument.entities())
                                .then(
                                        argument("amount", IntegerArgumentType.integer(0))
                                                .executes(
                                                        context -> {
                                                            return runAdjust(
                                                                    context.getSource(),
                                                                    EntityArgument.getEntities(context, "targets"),
                                                                    IntegerArgumentType.getInteger(context, "amount"),
                                                                    HeatingModes.ABSOLUTE,
                                                                    true
                                                            );
                                                        }
                                                )
                                                .then(
                                                        argument("mode", HeatingModeArgumentType.heatingMode())
                                                                .executes(context -> {
                                                                            return runAdjust(
                                                                                    context.getSource(),
                                                                                    EntityArgument.getEntities(context, "targets"),
                                                                                    IntegerArgumentType.getInteger(context, "amount"),
                                                                                    HeatingModeArgumentType.getHeatingMode(context, "mode"),
                                                                                    true
                                                                            );
                                                                        }
                                                                )
                                                )
                                )
                );

        var add = literal("add")
                .then(
                        argument("targets", EntityArgument.entities())
                                .then(
                                        argument("amount", IntegerArgumentType.integer(0))
                                                .executes(
                                                        context -> {
                                                            return runAdjust(
                                                                    context.getSource(),
                                                                    EntityArgument.getEntities(context, "targets"),
                                                                    IntegerArgumentType.getInteger(context, "amount"),
                                                                    HeatingModes.ABSOLUTE,
                                                                    false
                                                            );
                                                        }
                                                )
                                                .then(
                                                        argument("mode", HeatingModeArgumentType.heatingMode())
                                                                .executes(context -> {
                                                                            return runAdjust(
                                                                                    context.getSource(),
                                                                                    EntityArgument.getEntities(context, "targets"),
                                                                                    IntegerArgumentType.getInteger(context, "amount"),
                                                                                    HeatingModeArgumentType.getHeatingMode(context, "mode"),
                                                                                    false
                                                                            );
                                                                        }
                                                                )
                                                )
                                )
                );

        var setSubCommand = literal("set")
                .then(
                        argument("targets", EntityArgument.entities())
                                .then(
                                        argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    return runSet(context.getSource(),
                                                            EntityArgument.getEntities(context, "targets"),
                                                            IntegerArgumentType.getInteger(context, "amount"));
                                                })
                                )
                );

        return literal("thermoo").then(
                (literal("temperature").requires((src) -> src.hasPermission(2)))
                        .then(getSubCommand)
                        .then(remove)
                        .then(add)
                        .then(setSubCommand)
        );
    }

    private static int runGetScale(CommandSourceStack source, Entity target, int scale) throws CommandSyntaxException {
        if (target instanceof LivingEntity livingEntity) {
            float progress = livingEntity.thermoo$getTemperatureScale();
            int result = Mth.floor(progress * scale);

            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.temperature.get.scale.success",
                            target.getDisplayName(),
                            result
                    ), false
            );

            return result;
        } else {
            throw NOT_LIVING_ENTITY.create();
        }
    }

    private static int runGetMax(CommandSourceStack source, Entity target) throws CommandSyntaxException {

        if (target instanceof LivingEntity livingEntity) {
            int amount = livingEntity.thermoo$getMaxTemperature();
            source.sendSuccess(
                    () -> Component.translatable("commands.thermoo.temperature.get.max.success", target.getDisplayName(), amount),
                    false
            );
            return amount;
        } else {
            throw NOT_LIVING_ENTITY.create();
        }


    }

    private static int runGetMin(CommandSourceStack source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity livingEntity) {
            int amount = livingEntity.thermoo$getMinTemperature();
            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.temperature.get.min.success",
                            target.getDisplayName(),
                            amount
                    ),
                    false
            );
            return amount;
        } else {
            throw NOT_LIVING_ENTITY.create();
        }
    }

    private static int runGetCurrent(CommandSourceStack source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity livingEntity) {
            int amount = livingEntity.thermoo$getTemperature();
            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.thermoo.temperature.get.current.success",
                            target.getDisplayName(),
                            amount
                    ),
                    false
            );
            return amount;
        } else {
            throw NOT_LIVING_ENTITY.create();
        }
    }

    private static int runAdjust(CommandSourceStack source, Collection<? extends Entity> targets, int amount, HeatingModes mode, boolean isRemoving) throws CommandSyntaxException {
        amount = isRemoving ? -amount : amount;
        int sum = 0;
        for (Entity target : targets) {
            if (target instanceof TemperatureAware temperatureAware) {
                temperatureAware.thermoo$addTemperature(amount, mode);
                sum += amount;
            } else if (targets.size() == 1) {
                throw NOT_LIVING_ENTITY.create();
            }
        }


        Component msg;
        if (isRemoving) {
            if (targets.size() == 1) {
                var target = targets.iterator().next();
                msg = Component.translatable(
                        "commands.thermoo.temperature.remove.success.single",
                        amount,
                        target.getName(),
                        ((TemperatureAware) target).thermoo$getTemperature()
                );
            } else {
                msg = Component.translatable(
                        "commands.thermoo.temperature.remove.success.multiple",
                        amount,
                        targets.size()
                );
            }
        } else {
            if (targets.size() == 1) {
                var target = targets.iterator().next();
                msg = Component.translatable(
                        "commands.thermoo.temperature.add.success.single",
                        amount,
                        target.getName(),
                        ((TemperatureAware) target).thermoo$getTemperature()
                );
            } else {
                msg = Component.translatable(
                        "commands.thermoo.temperature.add.success.multiple",
                        amount,
                        targets.size()
                );
            }
        }

        source.sendSuccess(() -> msg, true);
        return sum;
    }

    private static int runSet(CommandSourceStack source, Collection<? extends Entity> targets, int amount) throws CommandSyntaxException {

        int sum = 0;
        for (Entity target : targets) {
            if (target instanceof LivingEntity livingEntity) {
                livingEntity.thermoo$setTemperature(amount);
                sum += amount;
            } else if (targets.size() == 1) {
                throw NOT_LIVING_ENTITY.create();
            }
        }

        Component msg;
        if (targets.size() == 1) {
            msg = Component.translatable(
                    "commands.thermoo.temperature.set.success.single",
                    targets.iterator().next().getName(),
                    amount
            );
        } else {
            msg = Component.translatable(
                    "commands.thermoo.temperature.set.success.multiple",
                    targets.size(),
                    amount
            );
        }
        source.sendSuccess(() -> msg, true);

        return sum;
    }

}

