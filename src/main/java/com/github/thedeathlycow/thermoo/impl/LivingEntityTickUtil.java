package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingMode;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTickContext;
import com.github.thedeathlycow.thermoo.mixin.common.accessor.EntityAccessor;
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
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            var context = new LivingEntityTickContextImpl(entity, serverWorld, getTemperatureTickPos(entity));
            tickChange(
                    context,
                    HeatingModes.PASSIVE,
                    LivingEntityTemperatureTickEvents.ALLOW_PASSIVE_TEMPERATURE_UPDATE,
                    LivingEntityTemperatureTickEvents.GET_PASSIVE_TEMPERATURE_CHANGE,
                    LivingEntityTemperatureTickEvents.ALLOW_PASSIVE_TEMPERATURE_CHANGE
            );

            tickChange(
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
     * @return returns a blockpos shifted up 0.1 blocks from the entity's current position
     */
    public static BlockPos getTemperatureTickPos(LivingEntity entity) {
        Vec3d pos = entity.getPos();
        if (entity.supportingBlockPos.isPresent()) {
            BlockPos blockPos = entity.supportingBlockPos.get();
            BlockState blockState = entity.getWorld().getBlockState(blockPos);
            return !blockState.isIn(BlockTags.FENCES) && !blockState.isIn(BlockTags.WALLS) && !(blockState.getBlock() instanceof FenceGateBlock)
                    ? blockPos.withY(MathHelper.floor(pos.y + 0.1f))
                    : blockPos;
        } else {
            return new BlockPos(
                    MathHelper.floor(pos.x),
                    MathHelper.floor(pos.y + 0.1f),
                    MathHelper.floor(pos.z)
            );
        }
    }

    private static void tickChange(
            LivingEntityTickContext context,
            HeatingMode heatingMode,
            Event<LivingEntityTemperatureTickEvents.AllowTemperatureUpdate> allowUpdate,
            Event<LivingEntityTemperatureTickEvents.GetTemperatureChange> getTempChange,
            Event<LivingEntityTemperatureTickEvents.AllowTemperatureChange> allowChange
    ) {
        if (allowUpdate.invoker().allowUpdate(context) == TriState.FALSE) {
            return;
        }

        int tempChange = getTempChange.invoker().addTemperature(context);
        if (allowChange.invoker().allowChange(context, tempChange) != TriState.FALSE) {
            context.affected().thermoo$addTemperature(tempChange, heatingMode);
        }
    }

    private record LivingEntityTickContextImpl(
            LivingEntity affected,
            ServerWorld world,
            BlockPos pos
    ) implements LivingEntityTickContext {

    }

    private LivingEntityTickUtil() {
    }
}