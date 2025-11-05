package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.CacheableFunction;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.commands.functions.InstantiatedFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class FunctionTemperatureEffect extends TemperatureEffect<FunctionTemperatureEffect.Config> {

    static final int DEFAULT_PERMISSION_LEVEL = 2;

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    CacheableFunction.CODEC
                            .fieldOf("function")
                            .forGetter(Config::function),
                    TagParser.AS_CODEC
                            .optionalFieldOf("arguments")
                            .forGetter(Config::arguments),
                    ExtraCodecs.POSITIVE_INT
                            .fieldOf("interval")
                            .orElse(20)
                            .forGetter(Config::interval),
                    Codec.intRange(0, 4)
                            .fieldOf("permission_level")
                            .orElse(DEFAULT_PERMISSION_LEVEL)
                            .forGetter(Config::permissionLevel)
            ).apply(instance, Config::new)
    );

    /**
     * @param configCodec Codec for the config type
     */
    public FunctionTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerLevel serverWorld, Config config) {
        MinecraftServer server = serverWorld.getServer();
        ServerFunctionManager functionManager = server.getFunctions();

        config.function.get(functionManager).ifPresent(
                func -> {
                    CommandSourceStack commandSource = victim.createCommandSourceStack()
                            .withSuppressedOutput()
                            .withPermission(config.permissionLevel);

                    this.execute(
                            func,
                            commandSource,
                            server,
                            config.arguments.orElse(null)
                    );
                }
        );

    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return config.interval <= 1 || victim.tickCount % config.interval == 0;
    }

    private void execute(
            CommandFunction<CommandSourceStack> function,
            CommandSourceStack source,
            MinecraftServer server,
            @Nullable CompoundTag arguments
    ) {
        ProfilerFiller profiler = server.getProfiler();
        profiler.push(() -> "function " + function.id());

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
            Thermoo.LOGGER.warn("Failed to apply macros to function {}", function.id(), e);
        } catch (Exception e) {
            Thermoo.LOGGER.warn("Failed to execute function {}", function.id(), e);
        } finally {
            profiler.pop();
        }
    }

    public record Config(
            CacheableFunction function,
            Optional<CompoundTag> arguments,
            int interval,
            int permissionLevel
    ) {

    }

}
