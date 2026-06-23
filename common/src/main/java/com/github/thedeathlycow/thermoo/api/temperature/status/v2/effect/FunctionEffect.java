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

package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectContext;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.CacheableFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Optional;

/**
 * An effect which invokes datapack functions ({@code .mcfunction} files).
 * <p>
 * Note: Datapack functions are generally not very performant. In general, you should prefer to create your own
 * temperature effect implementation when possible. This should only be used if you are limited to using datapacks
 * exclusively.
 */
public final class FunctionEffect implements TemperatureEffect {
    private static final int DEFAULT_PERMISSION_LEVEL = 2;

    /**
     * Codec for the function effect.
     */
    public static final MapCodec<FunctionEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    FunctionWithArguments.CODEC
                            .forGetter(FunctionEffect::function),
                    FunctionWithArguments.CODEC.codec()
                            .optionalFieldOf("remove_function")
                            .forGetter(FunctionEffect::removeFunction),
                    Codec.intRange(0, 4)
                            .optionalFieldOf("permission_level", DEFAULT_PERMISSION_LEVEL)
                            .forGetter(FunctionEffect::permissionLevel)
            ).apply(instance, FunctionEffect::new)
    );

    private final FunctionWithArguments function;
    private final Optional<FunctionWithArguments> removeFunction;
    private final int permissionLevel;

    private FunctionEffect(FunctionWithArguments function, Optional<FunctionWithArguments> removeFunction, int permissionLevel) {
        this.function = function;
        this.removeFunction = removeFunction;
        this.permissionLevel = permissionLevel;
    }

    /**
     * Creates a new function with macro arguments.
     *
     * @param functionId The ID of the function. May not be {@code null}.
     * @param arguments  The macro arguments of the function. If {@code null}, then no arguments will be supplied.
     * @throws NullPointerException if {@code functionId} is {@code null}
     */
    public static FunctionWithArguments function(Identifier functionId, @Nullable CompoundTag arguments) {
        Preconditions.checkNotNull(functionId, "Function ID may not be null");

        return new FunctionWithArguments(new CacheableFunction(functionId), Optional.ofNullable(arguments));
    }

    /**
     * Creates a new function without any macro arguments.
     *
     * @param functionId The ID of the function. May not be {@code null}.
     * @throws NullPointerException if {@code functionId} is {@code null}
     */
    public static FunctionWithArguments function(Identifier functionId) {
        return function(functionId, null);
    }

    /**
     * Creates a new builder with an apply function.
     *
     * @param function The apply function. May not be {@code null}.
     * @throws NullPointerException if {@code function} is {@code null}
     */
    public static Builder builder(FunctionWithArguments function) {
        Preconditions.checkNotNull(function, "Function may not be null");
        return new Builder(function);
    }

    /**
     * Creates a simple function effect which executes at a permission level of {@value #DEFAULT_PERMISSION_LEVEL} and
     * has no cleanup function,
     *
     * @param function The apply function. May not be {@code null}.
     * @throws NullPointerException if {@code function} is {@code null}
     */
    public static FunctionEffect create(FunctionWithArguments function) {
        return builder(function).build();
    }

    /**
     * Calls the main {@link #function()}
     *
     * @param target The entity receiving the effect.
     * @param context Additional context for the effect.
     * @return Returns {@code true} when executed on the logical server AND the function was successfully executed.
     */
    @Override
    public boolean apply(LivingEntity target, TemperatureEffectContext context) {
        if (target.level() instanceof ServerLevel serverLevel) {
            return this.function.createContextAndExecute(target, serverLevel, this.permissionLevel);
        }

        return false;
    }

    /**
     * Invokes the {@link #removeFunction()}, if present. Any potential cleanup logic should be implemented in that
     * function.
     *
     * @param target The entity the effect is being removed from.
     * @param context Additional context for the effect.
     */
    @Override
    public void remove(LivingEntity target, TemperatureEffectContext context) {
        if (this.removeFunction.isPresent() && target.level() instanceof ServerLevel serverLevel) {
            this.removeFunction.orElseThrow().createContextAndExecute(target, serverLevel, this.permissionLevel);
        }
    }

    /**
     * @return Returns {@link #CODEC}
     */
    @Override
    public MapCodec<FunctionEffect> codec() {
        return CODEC;
    }

    /**
     * The main function that applies the effect.
     */
    public FunctionWithArguments function() {
        return function;
    }

    /**
     * The function that handles cleanup logic for the effect. Optional.
     */
    public Optional<FunctionWithArguments> removeFunction() {
        return removeFunction;
    }

    /**
     * The permission level that the function is executed at.
     *
     * @return Returns an int between 0 and 4 (inclusive).
     */
    @Range(from = 0, to = 4)
    public int permissionLevel() {
        return permissionLevel;
    }

    /**
     * Builder object for creating function effects.
     */
    public static final class Builder {
        private final FunctionWithArguments function;
        @Nullable
        private FunctionWithArguments removeFunction;
        private int permissionLevel = DEFAULT_PERMISSION_LEVEL;

        private Builder(FunctionWithArguments function) {
            this.function = function;
        }

        /**
         * Add a remove function.
         *
         * @param removeFunction A datapack function that handles cleanup logic.
         * @return Returns this builder.
         * @throws NullPointerException if the {@code removeFunction} is {@code null}.
         */
        public Builder withRemoveFunction(FunctionWithArguments removeFunction) {
            Preconditions.checkNotNull(removeFunction, "Remove function may not be null");

            this.removeFunction = removeFunction;
            return this;
        }

        /**
         * Sets the permission level of the effect.
         *
         * @param value An int between 0 and 4 (inclusive)
         * @return Returns this builder.
         * @throws IllegalArgumentException if the {@code value} is not in the specified range.
         */
        public Builder withPermissionLevel(int value) {
            Preconditions.checkArgument(value >= 0 && value <= 4, "Permission level must be between 0 and 4 (inclusive)");

            this.permissionLevel = value;
            return this;
        }

        /**
         * Creates a new function effect from this builder.
         */
        public FunctionEffect build() {
            return new FunctionEffect(
                    this.function,
                    Optional.ofNullable(this.removeFunction),
                    this.permissionLevel
            );
        }
    }
}