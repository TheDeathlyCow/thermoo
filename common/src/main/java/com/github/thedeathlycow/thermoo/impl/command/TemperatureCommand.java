/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.impl.command;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusLookup;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.function.Function;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * Command relating to temperature. Allows temperature to be modified in game.
 * <p>
 * Usage:
 * <p>
 * {@code thermoo temperature <subcommand> <args>}
 */
public final class TemperatureCommand {
    private TemperatureCommand() {

    }

    static final SimpleCommandExceptionType NOT_LIVING_ENTITY = new SimpleCommandExceptionType(
            Component.translatable("commands.thermoo.temperature.exception.not_living_entity")
    );

    static final DynamicCommandExceptionType EFFECT_ALREADY_ENABLED = new DynamicCommandExceptionType(
            id -> Component.translatable("commands.thermoo.temperature.exception.effect_already_enabled", id.toString())
    );

    static final DynamicCommandExceptionType EFFECT_ALREADY_DISABLED = new DynamicCommandExceptionType(
            id -> Component.translatable("commands.thermoo.temperature.exception.effect_already_disabled", id.toString())
    );

    static final DynamicCommandExceptionType FAILED_TO_ENABLE_EFFECT = new DynamicCommandExceptionType(
            id -> Component.translatable("commands.thermoo.temperature.exception.failed_to_enable_effect", id.toString())
    );

    static final DynamicCommandExceptionType FAILED_TO_DISABLE_EFFECT = new DynamicCommandExceptionType(
            id -> Component.translatable("commands.thermoo.temperature.exception.failed_to_disable_effect", id.toString())
    );

    static final DynamicCommandExceptionType ERROR_TEMPERATURE_SOURCE_INVALID = new DynamicCommandExceptionType(
            value -> Component.translatableEscape("commands.thermoo.temperatre.source.invalid", value)
    );

    static final DynamicCommandExceptionType ERROR_TEMPERATURE_STATUS_INVALID = new DynamicCommandExceptionType(
            value -> Component.translatableEscape("commands.thermoo.temperatre.status.invalid", value)
    );

    public static LiteralArgumentBuilder<CommandSourceStack> create(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext buildContext,
            Commands.CommandSelection selection
    ) {
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


        var remove = literal("remove").then(adjustNode(buildContext, true));
        var add = literal("add").then(adjustNode(buildContext, false));

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

        var temperatureStatuses = buildContext.lookupOrThrow(ThermooRegistries.TEMPERATURE_STATUS);

        var status = literal("status")
                .then(literal("set_enabled")
                        .then(enableStatusNode(
                                buildContext,
                                _ -> argument("enabled", BoolArgumentType.bool())
                                        .executes(context -> {
                                                    return runStatusEnable(
                                                            context.getSource(),
                                                            EntityArgument.getEntities(context, "targets"),
                                                            temperatureStatuses.getOrThrow(ResourceKeyArgument.getRegistryKey(context, "status_id", ThermooRegistries.TEMPERATURE_STATUS, ERROR_TEMPERATURE_STATUS_INVALID)),
                                                            BoolArgumentType.getBool(context, "enabled")
                                                    );
                                                }
                                        ).build()
                        ))
                )
                .then(literal("enable")
                        .then(enableStatusNode(
                                buildContext,
                                node -> node.executes(context -> {
                                    return runStatusEnable(
                                            context.getSource(),
                                            EntityArgument.getEntities(context, "targets"),
                                            temperatureStatuses.getOrThrow(ResourceKeyArgument.getRegistryKey(context, "status_id", ThermooRegistries.TEMPERATURE_STATUS, ERROR_TEMPERATURE_STATUS_INVALID)),
                                            true
                                    );
                                }).build()
                        ))
                )
                .then(literal("disable")
                        .then(enableStatusNode(
                                buildContext,
                                node -> node.executes(context -> {
                                    return runStatusEnable(
                                            context.getSource(),
                                            EntityArgument.getEntities(context, "targets"),
                                            temperatureStatuses.getOrThrow(ResourceKeyArgument.getRegistryKey(context, "status_id", ThermooRegistries.TEMPERATURE_STATUS, ERROR_TEMPERATURE_STATUS_INVALID)),
                                            false
                                    );
                                }).build()
                        ))
                );
        return literal("thermoo").then(
                (literal("temperature").requires((src) -> src.permissions()
                        .hasPermission(Permissions.COMMANDS_GAMEMASTER)))
                        .then(getSubCommand)
                        .then(remove)
                        .then(add)
                        .then(setSubCommand)
                        .then(status)
        );
    }

    private static RequiredArgumentBuilder<CommandSourceStack, ?> enableStatusNode(
            CommandBuildContext buildContext,
            Function<ArgumentBuilder<CommandSourceStack, ?>, CommandNode<CommandSourceStack>> then
    ) {
        var tail = argument("status_id", ResourceKeyArgument.key(ThermooRegistries.TEMPERATURE_STATUS));
        tail.then(then.apply(tail));

        return argument("targets", EntityArgument.entities()).then(tail);
    }

    private static RequiredArgumentBuilder<CommandSourceStack, EntitySelector> adjustNode(CommandBuildContext buildContext, boolean removing) {
        var temperatureSources = buildContext.lookupOrThrow(ThermooRegistries.TEMPERATURE_SOURCE);

        return argument("targets", EntityArgument.entities())
                .then(argument("amount", IntegerArgumentType.integer(0))
                        .executes(context -> {
                            return runAdjust(
                                    context.getSource(),
                                    EntityArgument.getEntities(context, "targets"),
                                    IntegerArgumentType.getInteger(context, "amount"),
                                    context.getSource().getLevel().thermoo$temperatureSources().absolute(),
                                    removing
                            );
                        })
                        .then(argument("source", ResourceKeyArgument.key(ThermooRegistries.TEMPERATURE_SOURCE))
                                .executes(context -> {
                                    return runAdjust(
                                            context.getSource(),
                                            EntityArgument.getEntities(context, "targets"),
                                            IntegerArgumentType.getInteger(context, "amount"),
                                            TemperatureChange.create(
                                                    temperatureSources.getOrThrow(ResourceKeyArgument.getRegistryKey(context, "source", ThermooRegistries.TEMPERATURE_SOURCE, ERROR_TEMPERATURE_SOURCE_INVALID))
                                            ),
                                            removing
                                    );
                                })
                                .then(literal("by")
                                        .then(argument("direct_cause", EntityArgument.entity())
                                                .executes(context -> {
                                                    return runAdjust(
                                                            context.getSource(),
                                                            EntityArgument.getEntities(context, "targets"),
                                                            IntegerArgumentType.getInteger(context, "amount"),
                                                            TemperatureChange.create(
                                                                    temperatureSources.getOrThrow(ResourceKeyArgument.getRegistryKey(context, "source", ThermooRegistries.TEMPERATURE_SOURCE, ERROR_TEMPERATURE_SOURCE_INVALID)),
                                                                    EntityArgument.getEntity(context, "direct_cause")
                                                            ),
                                                            removing
                                                    );
                                                })
                                                .then(literal("from")
                                                        .then(argument("cause", EntityArgument.entity())
                                                                .executes(context -> {
                                                                    return runAdjust(
                                                                            context.getSource(),
                                                                            EntityArgument.getEntities(context, "targets"),
                                                                            IntegerArgumentType.getInteger(context, "amount"),
                                                                            TemperatureChange.create(
                                                                                    temperatureSources.getOrThrow(ResourceKeyArgument.getRegistryKey(context, "source", ThermooRegistries.TEMPERATURE_SOURCE, ERROR_TEMPERATURE_SOURCE_INVALID)),
                                                                                    EntityArgument.getEntity(context, "cause"),
                                                                                    EntityArgument.getEntity(context, "direct_cause")
                                                                            ),
                                                                            removing
                                                                    );
                                                                })
                                                        )
                                                )
                                        )
                                        .then(literal("at")
                                                .then(argument("position", Vec3Argument.vec3())
                                                        .executes(context -> {
                                                            return runAdjust(
                                                                    context.getSource(),
                                                                    EntityArgument.getEntities(context, "targets"),
                                                                    IntegerArgumentType.getInteger(context, "amount"),
                                                                    TemperatureChange.create(
                                                                            temperatureSources.getOrThrow(ResourceKeyArgument.getRegistryKey(context, "source", ThermooRegistries.TEMPERATURE_SOURCE, ERROR_TEMPERATURE_SOURCE_INVALID)),
                                                                            Vec3Argument.getVec3(context, "position")
                                                                    ),
                                                                    removing
                                                            );
                                                        })
                                                )
                                        )
                                )
                        )
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

    private static int runAdjust(CommandSourceStack source, Collection<? extends Entity> targets, int amount, TemperatureChange change, boolean isRemoving) throws CommandSyntaxException {
        amount = isRemoving ? -amount : amount;
        int sum = 0;
        for (Entity target : targets) {
            if (target instanceof TemperatureAware temperatureAware) {
                temperatureAware.thermoo$addTemperature(amount, change);
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

    private static int runStatusEnable(CommandSourceStack source, Collection<? extends Entity> entities, Holder.Reference<TemperatureStatus> status, boolean enabled) throws CommandSyntaxException {
        if (entities.size() == 1) {
            return runStatusEnableSingle(source, entities.iterator().next(), status, enabled);
        }

        int totalAffected = 0;

        for (Entity entity : entities) {
            if (TemperatureStatusLookup.setEnabled(entity, status, enabled)) {
                totalAffected++;
            }
        }

        final int result = totalAffected;

        if (result == 0) {
            throw enabled ? FAILED_TO_ENABLE_EFFECT.create(status.key().identifier()) : FAILED_TO_DISABLE_EFFECT.create(status.key().identifier());
        }

        if (enabled) {
            source.sendSuccess(() -> Component.translatable("commands.thermoo.temperature.effect.multiple.set_enabled.true", status.key().identifier().toString(), result), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.thermoo.temperature.effect.multiple.set_enabled.false", status.key().identifier().toString(), result), true);
        }

        return result;
    }

    private static int runStatusEnableSingle(CommandSourceStack source, Entity entity, Holder.Reference<TemperatureStatus> status, boolean enabled) throws CommandSyntaxException {
        if (TemperatureStatusLookup.isEnabled(entity, status) == enabled) {
            throw enabled ? EFFECT_ALREADY_ENABLED.create(status.key().identifier()) : EFFECT_ALREADY_DISABLED.create(status.key().identifier());
        }

        if (TemperatureStatusLookup.setEnabled(entity, status, enabled)) {
            if (enabled) {
                source.sendSuccess(() -> Component.translatable("commands.thermoo.temperature.effect.single.set_enabled.true", status.key().identifier().toString(), entity.getDisplayName()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("commands.thermoo.temperature.effect.single.set_enabled.false", status.key().identifier().toString(), entity.getDisplayName()), true);
            }
        } else {
            throw enabled ? FAILED_TO_ENABLE_EFFECT.create(status.key().identifier()) : FAILED_TO_DISABLE_EFFECT.create(status.key().identifier());
        }

        return Command.SINGLE_SUCCESS;
    }
}

