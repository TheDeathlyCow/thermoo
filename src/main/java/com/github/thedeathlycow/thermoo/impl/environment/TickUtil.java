package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.impl.LivingEntityTickUtil;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.component.ComponentMap;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public final class TickUtil {
    public static void tickPlayerTemperature(ServerPlayerEntity player) {
        if (player.isDead() || player.isRemoved()) {
            return;
        }

        final EnvironmentTickContextImpl<ServerPlayerEntity> context = new EnvironmentTickContextImpl<>(
                player,
                player.getServerWorld(),
                LivingEntityTickUtil.getTemperatureTickPos(player)
        );
        if (ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_UPDATE.invoker().allowUpdate(context) == TriState.FALSE) {
            return;
        }

        final var lookup = EnvironmentLookup.getInstance();
        context.components = lookup.findEnvironmentComponents(context.world, context.pos);

        int temperatureChange = ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.invoker().addPointChange(context);

        if (temperatureChange != 0 && invokeAllowChange(context, temperatureChange)) {
            player.thermoo$addTemperature(temperatureChange, HeatingModes.PASSIVE);
        }
    }

    private static boolean invokeAllowChange(EnvironmentTickContext<ServerPlayerEntity> context, int temperatureChange) {
        TriState result = ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_CHANGE.invoker()
                .allowTemperatureChange(context, temperatureChange);
        return result != TriState.FALSE;
    }

    private static class EnvironmentTickContextImpl<T extends TemperatureAware> implements EnvironmentTickContext<T> {
        private final T affected;
        private final ServerWorld world;
        private final BlockPos pos;
        private ComponentMap components = ComponentMap.EMPTY;

        public EnvironmentTickContextImpl(T affected, ServerWorld world, BlockPos pos) {
            this.affected = affected;
            this.world = world;
            this.pos = pos;
        }

        @Override
        public @NotNull T affected() {
            return this.affected;
        }

        @Override
        public @NotNull ServerWorld world() {
            return this.world;
        }

        @Override
        public @NotNull BlockPos pos() {
            return this.pos;
        }

        @Override
        public ComponentMap components() {
            return this.components;
        }
    }

    private TickUtil() {
    }
}