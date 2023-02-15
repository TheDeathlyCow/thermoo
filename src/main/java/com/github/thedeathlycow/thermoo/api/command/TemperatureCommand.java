package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Command relating to temperature. Allows temperature to be modified in game.
 * <p>
 * Usage:
 * <p>
 * {@code thermoo temperature <subcommand> <args>}
 */
public class TemperatureCommand {

    private static final SimpleCommandExceptionType NOT_LIVING_ENTITY = new SimpleCommandExceptionType(Text.translatable("commands.thermoo.temperature.exception.not_living_entity"));

    /**
     * Supplier for creating a new temperature command builder to be registered to the Minecraft server
     * <p>
     * Registered by the default implementation of this API.
     */
    public static final Supplier<LiteralArgumentBuilder<ServerCommandSource>> COMMAND_BUILDER = TemperatureCommand::buildCommand;

    @Contract("->new")
    private static LiteralArgumentBuilder<ServerCommandSource> buildCommand() {
        var getSubCommand = literal("get")
                .then(
                        argument("target", EntityArgumentType.entity())
                                .executes(context -> {
                                    return runGetCurrent(
                                            context.getSource(),
                                            EntityArgumentType.getEntity(context, "target")
                                    );
                                })
                                .then(literal("current")
                                        .executes(context -> {
                                            return runGetCurrent(
                                                    context.getSource(),
                                                    EntityArgumentType.getEntity(context, "target")
                                            );
                                        })
                                )
                                .then(literal("max")
                                        .executes(context -> {
                                            return runGetMax(
                                                    context.getSource(),
                                                    EntityArgumentType.getEntity(context, "target")
                                            );
                                        })
                                )
                                .then(literal("min")
                                        .executes(context -> {
                                            return runGetMin(
                                                    context.getSource(),
                                                    EntityArgumentType.getEntity(context, "target")
                                            );
                                        })
                                )
                                .then(literal("scale")
                                        .executes(context -> {
                                            return runGetScale(
                                                    context.getSource(),
                                                    EntityArgumentType.getEntity(context, "target"),
                                                    100
                                            );
                                        })
                                        .then(argument("scale", IntegerArgumentType.integer(1))
                                                .executes(context -> {
                                                    return runGetScale(
                                                            context.getSource(),
                                                            EntityArgumentType.getEntity(context, "target"),
                                                            IntegerArgumentType.getInteger(context, "scale")
                                                    );
                                                })
                                        )
                                )

                );


        var remove = literal("remove")
                .then(
                        argument("targets", EntityArgumentType.entities())
                                .then(
                                        argument("amount", IntegerArgumentType.integer(0))
                                                .executes(
                                                        context -> {
                                                            return runAdjust(
                                                                    context.getSource(),
                                                                    EntityArgumentType.getEntities(context, "targets"),
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
                                                                                    EntityArgumentType.getEntities(context, "targets"),
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
                        argument("targets", EntityArgumentType.entities())
                                .then(
                                        argument("amount", IntegerArgumentType.integer(0))
                                                .executes(
                                                        context -> {
                                                            return runAdjust(
                                                                    context.getSource(),
                                                                    EntityArgumentType.getEntities(context, "targets"),
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
                                                                                    EntityArgumentType.getEntities(context, "targets"),
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
                        argument("targets", EntityArgumentType.entities())
                                .then(
                                        argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    return runSet(context.getSource(),
                                                            EntityArgumentType.getEntities(context, "targets"),
                                                            IntegerArgumentType.getInteger(context, "amount"));
                                                })
                                )
                );

        return literal("thermoo").then(
                (literal("temperature").requires((src) -> src.hasPermissionLevel(2)))
                        .then(getSubCommand)
                        .then(remove)
                        .then(add)
                        .then(setSubCommand)
        );
    }

    private static int runGetScale(ServerCommandSource source, Entity target, int scale) throws CommandSyntaxException {
        if (target instanceof LivingEntity livingEntity) {
            float progress = livingEntity.thermoo$getTemperatureScale();
            int result = MathHelper.floor(progress * scale);

            Text msg = Text.translatable("commands.thermoo.temperature.get.scale.success", target.getDisplayName(), result);
            source.sendFeedback(msg, false);

            return result;
        } else {
            throw NOT_LIVING_ENTITY.create();
        }
    }

    private static int runGetMax(ServerCommandSource source, Entity target) throws CommandSyntaxException {

        if (target instanceof LivingEntity livingEntity) {
            int amount = livingEntity.thermoo$getMaxTemperature();
            Text msg = Text.translatable("commands.thermoo.temperature.get.max.success", target.getDisplayName(), amount);
            source.sendFeedback(msg, false);
            return amount;
        } else {
            throw NOT_LIVING_ENTITY.create();
        }


    }

    private static int runGetMin(ServerCommandSource source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity livingEntity) {
            int amount = livingEntity.thermoo$getMinTemperature();
            Text msg = Text.translatable("commands.thermoo.temperature.get.min.success", target.getDisplayName(), amount);
            source.sendFeedback(msg, false);
            return amount;
        } else {
            throw NOT_LIVING_ENTITY.create();
        }
    }

    private static int runGetCurrent(ServerCommandSource source, Entity target) throws CommandSyntaxException {
        if (target instanceof LivingEntity livingEntity) {
            int amount = livingEntity.thermoo$getTemperature();
            Text msg = Text.translatable("commands.thermoo.temperature.get.current.success", target.getDisplayName(), amount);
            source.sendFeedback(msg, false);
            return amount;
        } else {
            throw NOT_LIVING_ENTITY.create();
        }
    }

    private static int runAdjust(ServerCommandSource source, Collection<? extends Entity> targets, int amount, HeatingModes mode, boolean isRemoving) throws CommandSyntaxException {
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


        Text msg;
        if (isRemoving) {
            if (targets.size() == 1) {
                var target = targets.iterator().next();
                msg = Text.translatable(
                        "commands.thermoo.temperature.remove.success.single",
                        amount,
                        target.getName(),
                        ((TemperatureAware) target).thermoo$getTemperature()
                );
            } else {
                msg = Text.translatable(
                        "commands.thermoo.temperature.remove.success.multiple",
                        amount,
                        targets.size()
                );
            }
        } else {
            if (targets.size() == 1) {
                var target = targets.iterator().next();
                msg = Text.translatable(
                        "commands.thermoo.temperature.add.success.single",
                        amount,
                        target.getName(),
                        ((TemperatureAware) target).thermoo$getTemperature()
                );
            } else {
                msg = Text.translatable(
                        "commands.thermoo.temperature.add.success.multiple",
                        amount,
                        targets.size()
                );
            }
        }

        source.sendFeedback(msg, true);
        return sum;
    }

    private static int runSet(ServerCommandSource source, Collection<? extends Entity> targets, int amount) throws CommandSyntaxException {

        int sum = 0;
        for (Entity target : targets) {
            if (target instanceof LivingEntity livingEntity) {
                livingEntity.thermoo$setTemperature(amount);
                sum += amount;
            } else if (targets.size() == 1) {
                throw NOT_LIVING_ENTITY.create();
            }
        }

        Text msg;
        if (targets.size() == 1) {
            msg = Text.translatable(
                    "commands.thermoo.temperature.set.success.single",
                    targets.iterator().next().getName(),
                    amount
            );
        } else {
            msg = Text.translatable(
                    "commands.thermoo.temperature.set.success.multiple",
                    targets.size(),
                    amount
            );
        }
        source.sendFeedback(msg, true);

        return sum;
    }

}

