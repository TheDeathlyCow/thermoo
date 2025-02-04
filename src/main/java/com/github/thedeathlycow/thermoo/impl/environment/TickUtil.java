package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public final class TickUtil {
    public static void tickPlayerTemperature(ServerPlayerEntity player) {
        final var lookup = EnvironmentLookup.getInstance();

        final ServerWorld world = player.getServerWorld();
        final BlockPos pos = player.getBlockPos();
        final var unit = TemperatureUnit.CELSIUS;
        final var temperature = new TemperatureRecord(
                lookup.findTemperature(world, pos, unit),
                unit
        );
        final double relativeHumidity = lookup.findRelativeHumidity(world, pos);
        final var context = new EnvironmentTickContextImpl<ServerPlayerEntity>(
                player,
                world,
                pos,
                temperature,
                relativeHumidity
        );

        int temperatureChange = ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.invoker().addPointChange(context);

        if (temperatureChange != 0 && invokeAllowChange(context, temperatureChange)) {
            player.thermoo$addTemperature(temperatureChange, HeatingModes.PASSIVE);
        }
    }

    private static boolean invokeAllowChange(EnvironmentTickContext<ServerPlayerEntity> context, int temperatureChange) {
        TriState result = ServerPlayerEnvironmentTickEvents.ALLOW_PLAYER_TEMPERATURE_CHANGE.invoker()
                .allowTemperatureChange(context, temperatureChange);
        return result != TriState.FALSE;
    }

    private record EnvironmentTickContextImpl<T extends TemperatureAware>(
            T affected,
            ServerWorld world,
            BlockPos pos,
            TemperatureRecord temperature,
            double relativeHumidity
    ) implements EnvironmentTickContext<T> {

    }

    private TickUtil() {
    }
}