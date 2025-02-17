package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingMode;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntitySoakingTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.TickContext;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class LivingEntityTickUtil {
    public static void tick(LivingEntity entity) {
        if (entity.isDead() || entity.isRemoved()) {
            return;
        }

        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            var context = new TickContextImpl(entity, serverWorld, getTemperatureTickPos(entity));
            tickSoakingChange(
                    context,
                    LivingEntitySoakingTickEvents.ALLOW_SOAKING_UPDATE,
                    LivingEntitySoakingTickEvents.ADD_SOAKING_CHANGE,
                    LivingEntitySoakingTickEvents.ALLOW_SOAKING_CHANGE
            );
            tickTemperatureChange(
                    context,
                    HeatingModes.PASSIVE,
                    LivingEntityTemperatureTickEvents.ALLOW_PASSIVE_TEMPERATURE_UPDATE,
                    LivingEntityTemperatureTickEvents.GET_PASSIVE_TEMPERATURE_CHANGE,
                    LivingEntityTemperatureTickEvents.ALLOW_PASSIVE_TEMPERATURE_CHANGE
            );

            tickTemperatureChange(
                    context,
                    HeatingModes.ACTIVE,
                    LivingEntityTemperatureTickEvents.ALLOW_ACTIVE_TEMPERATURE_UPDATE,
                    LivingEntityTemperatureTickEvents.GET_ACTIVE_TEMPERATURE_CHANGE,
                    LivingEntityTemperatureTickEvents.ALLOW_ACTIVE_TEMPERATURE_CHANGE
            );
        }
    }

    /**
     * used to offset effects like being stuck in mud
     *
     * @return returns a blockpos shifted up 0.21 blocks from the entity's current position
     */
    public static BlockPos getTemperatureTickPos(LivingEntity entity) {
        Vec3d pos = entity.getPos();
        final float offset = 0.21f;
        if (entity.supportingBlockPos.isPresent()) {
            BlockPos blockPos = entity.supportingBlockPos.get();
            BlockState blockState = entity.getWorld().getBlockState(blockPos);
            return !blockState.isIn(BlockTags.FENCES) && !blockState.isIn(BlockTags.WALLS) && !(blockState.getBlock() instanceof FenceGateBlock)
                    ? blockPos.withY(MathHelper.floor(pos.y + offset))
                    : blockPos;
        } else {
            return new BlockPos(
                    MathHelper.floor(pos.x),
                    MathHelper.floor(pos.y + offset),
                    MathHelper.floor(pos.z)
            );
        }
    }

    private static void tickTemperatureChange(
            TickContext<LivingEntity> context,
            HeatingMode heatingMode,
            Event<LivingEntityTemperatureTickEvents.AllowTemperatureUpdate> allowUpdate,
            Event<LivingEntityTemperatureTickEvents.GetTemperatureChange> getTempChange,
            Event<LivingEntityTemperatureTickEvents.AllowTemperatureChange> allowChange
    ) {
        if (allowUpdate.invoker().allowUpdate(context) == TriState.FALSE) {
            return;
        }

        int tempChange = getTempChange.invoker().addTemperature(context);
        if (tempChange != 0 && allowChange.invoker().allowChange(context, tempChange) != TriState.FALSE) {
            context.affected().thermoo$addTemperature(tempChange, heatingMode);
        }
    }

    private static void tickSoakingChange(
            TickContext<LivingEntity> context,
            Event<LivingEntitySoakingTickEvents.AllowSoakingUpdate> allowUpdate,
            Event<LivingEntitySoakingTickEvents.GetSoakingChange> addSoakChange,
            Event<LivingEntitySoakingTickEvents.AllowSoakingChange> allowChange
    ) {
        if (allowUpdate.invoker().allowUpdate(context) == TriState.FALSE) {
            return;
        }

        int soakingChange = addSoakChange.invoker().addChange(context);

        if (soakingChange != 0 && allowChange.invoker().allowChange(context, soakingChange) != TriState.FALSE) {
            context.affected().thermoo$addWetTicks(soakingChange);
        }
    }

    private record TickContextImpl(
            LivingEntity affected,
            ServerWorld world,
            BlockPos pos
    ) implements TickContext<LivingEntity> {

    }

    private LivingEntityTickUtil() {
    }
}