package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.*;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.commands.functions.InstantiatedFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
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
     */
    public static FunctionWithArguments function(Identifier functionId, @Nullable CompoundTag arguments) {
        Preconditions.checkNotNull(functionId, "Function ID may not be null");

        return new FunctionWithArguments(new CacheableFunction(functionId), Optional.ofNullable(arguments));
    }

    /**
     * Creates a new function without any macro arguments.
     */
    public static FunctionWithArguments function(Identifier functionId) {
        return function(functionId, null);
    }

    /**
     * Creates a new builder with an apply function.
     */
    public static Builder builder(FunctionWithArguments function) {
        Preconditions.checkNotNull(function, "Function may not be null");
        return new Builder(function);
    }

    /**
     * Creates a simple function effect which executes at a permission level of {@value #DEFAULT_PERMISSION_LEVEL} and
     * has no cleanup function,
     */
    public static FunctionEffect create(FunctionWithArguments function) {
        return builder(function).build();
    }

    /**
     * Calls the main {@link #function()}
     *
     * @param target The entity receiving the effect.
     * @param level  The level the entity is in.
     * @return Returns {@code true} when executed on the logical server AND the function was successfully executed.
     */
    @Override
    public boolean apply(LivingEntity target, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return this.function.createContextAndExecute(target, serverLevel, this.permissionLevel);
        }

        return false;
    }

    /**
     * Invokes the {@link #removeFunction()}, if present. Any potential cleanup logic should be implemented in that
     * function.
     *
     * @param target The entity the effect is being removed from.
     * @param level  The level the entity is currently in.
     */
    @Override
    public void remove(LivingEntity target, Level level) {
        if (this.removeFunction.isPresent() && level instanceof ServerLevel serverLevel) {
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
         * @return Returns this builder.
         */
        public Builder withRemoveFunction(FunctionWithArguments removeFunction) {
            Preconditions.checkNotNull(removeFunction, "Remove function may not be null");

            this.removeFunction = removeFunction;
            return this;
        }

        /**
         * Sets the permission level of the effect.
         *
         * @return Returns this builder.
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