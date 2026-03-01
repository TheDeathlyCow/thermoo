package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.*;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.commands.functions.InstantiatedFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * An object that stores a function ID and, optionally, its macro arguments, and handles logic for its execution.
 */
public final class FunctionWithArguments {
    /**
     * The codec for this object.
     */
    public static final MapCodec<FunctionWithArguments> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    CacheableFunction.CODEC
                            .fieldOf("function")
                            .forGetter(FunctionWithArguments::function),
                    TagParser.FLATTENED_CODEC
                            .optionalFieldOf("arguments")
                            .forGetter(FunctionWithArguments::arguments)
            ).apply(instance, FunctionWithArguments::new)
    );

    private final CacheableFunction function;

    private final Optional<CompoundTag> arguments;

    FunctionWithArguments(CacheableFunction function, Optional<CompoundTag> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    /**
     * The function to execute.
     */
    public CacheableFunction function() {
        return function;
    }

    /**
     * The macro arguments of the function.
     */
    public Optional<CompoundTag> arguments() {
        return arguments;
    }

    boolean createContextAndExecute(
            LivingEntity target,
            ServerLevel level,
            int permissionLevel
    ) {
        MinecraftServer server = level.getServer();
        ServerFunctionManager functionManager = server.getFunctions();
        return this.function.get(functionManager).map(
                function -> {
                    PermissionSet permissionSet = LevelBasedPermissionSet.forLevel(PermissionLevel.byId(permissionLevel));

                    CommandSourceStack commandSource = target.createCommandSourceStackForNameResolution(level)
                            .withSuppressedOutput()
                            .withPermission(permissionSet);

                    return this.execute(
                            function,
                            commandSource,
                            level.getServer(),
                            this.arguments.orElse(null)
                    );
                }
        ).orElse(false);
    }

    private boolean execute(
            CommandFunction<CommandSourceStack> function,
            CommandSourceStack source,
            MinecraftServer server,
            @Nullable CompoundTag arguments
    ) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push(() -> "function " + function.id());
        boolean executed = true;

        try {
            InstantiatedFunction<CommandSourceStack> procedure = function.instantiate(
                    arguments,
                    server.getCommands().getDispatcher()
            );
            Commands.executeCommandInContext(
                    source,
                    context -> ExecutionContext.queueInitialFunctionCall(
                            context,
                            procedure,
                            source,
                            CommandResultCallback.EMPTY
                    )
            );
        } catch (FunctionInstantiationException e) {
            Thermoo.LOGGER.warn("Failed to instantiate function {}", function.id(), e);
            executed = false;
        } catch (Exception e) {
            Thermoo.LOGGER.warn("Failed to execute function {}", function.id(), e);
            executed = false;
        }

        profiler.pop();
        return executed;
    }
}