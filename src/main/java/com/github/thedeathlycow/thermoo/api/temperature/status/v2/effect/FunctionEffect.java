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

import java.util.Optional;

public final class FunctionEffect implements TemperatureEffect {
    static final int DEFAULT_PERMISSION_LEVEL = 2;

    public static final MapCodec<FunctionEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    CacheableFunction.CODEC
                            .fieldOf("function")
                            .forGetter(FunctionEffect::function),
                    TagParser.FLATTENED_CODEC
                            .optionalFieldOf("arguments")
                            .forGetter(FunctionEffect::arguments),
                    Codec.intRange(0, 4)
                            .optionalFieldOf("permission_level", DEFAULT_PERMISSION_LEVEL)
                            .forGetter(FunctionEffect::permissionLevel)
            ).apply(instance, FunctionEffect::new)
    );

    private final CacheableFunction function;
    private final Optional<CompoundTag> arguments;
    private final int permissionLevel;

    private FunctionEffect(CacheableFunction function, Optional<CompoundTag> arguments, int permissionLevel) {
        this.function = function;
        this.arguments = arguments;
        this.permissionLevel = permissionLevel;
    }

    public static Builder builder(CacheableFunction function) {
        Preconditions.checkNotNull(function, "Function may not be null");
        return new Builder(function);
    }

    public static Builder builder(Identifier functionId) {
        return builder(new CacheableFunction(functionId));
    }

    @Override
    public boolean apply(LivingEntity target, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();
            ServerFunctionManager functionManager = server.getFunctions();

            return this.function.get(functionManager).map(
                    func -> {
                        PermissionSet permissionSet = LevelBasedPermissionSet.forLevel(PermissionLevel.byId(this.permissionLevel));

                        CommandSourceStack commandSource = target.createCommandSourceStackForNameResolution(serverLevel)
                                .withSuppressedOutput()
                                .withPermission(permissionSet);

                        return this.execute(
                                func,
                                commandSource,
                                server,
                                this.arguments.orElse(null)
                        );
                    }
            ).orElse(false);
        }

        return false;
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

    @Override
    public MapCodec<FunctionEffect> codec() {
        return CODEC;
    }

    public CacheableFunction function() {
        return function;
    }

    public Optional<CompoundTag> arguments() {
        return arguments;
    }

    public int permissionLevel() {
        return permissionLevel;
    }

    public static final class Builder {
        private final CacheableFunction function;
        @Nullable
        private CompoundTag arguments = null;
        @Nullable
        private Integer permissionLevel = null;

        private Builder(CacheableFunction function) {
            this.function = function;
        }

        public Builder withArguments(CompoundTag arguments) {
            Preconditions.checkNotNull(arguments);
            Preconditions.checkState(this.arguments == null, "Arguments already defined");

            this.arguments = arguments;
            return this;
        }

        public Builder withPermissionLevel(int value) {
            Preconditions.checkArgument(value >= 0 && value <= 4, "Permission level must be between 0 and 4 (inclusive)");
            Preconditions.checkState(this.permissionLevel == null, "Permission level already defined");

            this.permissionLevel = value;
            return this;
        }

        public FunctionEffect build() {
            return new FunctionEffect(
                    this.function,
                    Optional.ofNullable(this.arguments),
                    this.permissionLevel != null ? this.permissionLevel : DEFAULT_PERMISSION_LEVEL
            );
        }
    }
}