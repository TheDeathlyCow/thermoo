package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.command.CommandExecutionContext;
import net.minecraft.command.ReturnValueConsumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FunctionTemperatureEffect extends TemperatureEffect<FunctionTemperatureEffect.Config> {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    LazyContainer.CODEC
                            .fieldOf("function")
                            .forGetter(Config::function),
                    StringNbtReader.STRINGIFIED_CODEC
                            .optionalFieldOf("arguments")
                            .forGetter(Config::arguments),
                    Codecs.POSITIVE_INT
                            .fieldOf("interval")
                            .orElse(20)
                            .forGetter(Config::interval),
                    Codec.intRange(0, 4)
                            .fieldOf("permission_level")
                            .orElse(2)
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
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        MinecraftServer server = serverWorld.getServer();
        CommandFunctionManager functionManager = server.getCommandFunctionManager();

        config.function.get(functionManager).ifPresent(
                func -> {
                    ServerCommandSource commandSource = victim.getCommandSource()
                            .withSilent()
                            .withLevel(config.permissionLevel);

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
        return config.interval <= 1 || victim.age % config.interval == 0;
    }

    private void execute(
            CommandFunction<ServerCommandSource> function,
            ServerCommandSource source,
            MinecraftServer server,
            @Nullable NbtCompound arguments
    ) {
        Profiler profiler = server.getProfiler();
        profiler.push(() -> "function " + function.id());

        try {
            Procedure<ServerCommandSource> procedure = function.withMacroReplaced(
                    arguments,
                    server.getCommandManager().getDispatcher(),
                    source
            );
            CommandManager.callWithContext(
                    source,
                    context -> CommandExecutionContext.enqueueProcedureCall(
                            context,
                            procedure,
                            source,
                            ReturnValueConsumer.EMPTY
                    )
            );
        } catch (MacroException e) {
            Thermoo.LOGGER.warn("Failed to apply macros to function {}", function.id(), e);
        } catch (Exception e) {
            Thermoo.LOGGER.warn("Failed to execute function {}", function.id(), e);
        } finally {
            profiler.pop();
        }
    }

    public record Config(
            LazyContainer function,
            Optional<NbtCompound> arguments,
            int interval,
            int permissionLevel
    ) {

    }

}
