package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.core.v1.registry.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.core.v1.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.core.v1.event.LivingEntitySoakingTickEvents;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.impl.component.ThermooComponents;
import com.github.thedeathlycow.thermoo.impl.core.UpdateEvents;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentTickContextImpl;
import com.github.thedeathlycow.thermoo.impl.environment.ServerPlayerTickUtil;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


public final class LivingEntityTickUtil {
    public static void tick(LivingEntity entity) {
        if (entity.isDeadOrDying() || entity.isRemoved()) {
            return;
        }

        if (entity.level() instanceof ServerLevel serverLevel) {
            BlockPos pos = getTemperatureTickPos(entity);
            if (entity instanceof ServerPlayer player) {
                EnvironmentTickContext<ServerPlayer> context = new EnvironmentTickContextImpl<>(
                        player,
                        serverLevel,
                        pos,
                        EnvironmentLookup.getInstance().findEnvironmentComponents(serverLevel, pos)
                );
                invokeEntityEvents(context, serverLevel.holderLookup(ThermooRegistryKeys.TEMPERATURE_SOURCE));
                ServerPlayerTickUtil.invokePlayerTemperatureEvents(context);
            } else {
                EnvironmentTickContext<LivingEntity> context = new EnvironmentTickContextImpl<>(
                        entity,
                        serverLevel,
                        pos,
                        DataComponentMap.EMPTY
                );
                invokeEntityEvents(context, serverLevel.holderLookup(ThermooRegistryKeys.TEMPERATURE_SOURCE));
            }

            boolean isSyncTick = entity.tickCount % 20 == 0;

            if (isSyncTick || ThermooComponents.TEMPERATURE.get(entity).isDirty()) {
                ThermooComponents.TEMPERATURE.sync(entity);
            }

            if (isSyncTick || ThermooComponents.WETNESS.get(entity).isDirty()) {
                ThermooComponents.WETNESS.sync(entity);
            }
        }
    }

    /**
     * used to offset effects like being stuck in mud
     *
     * @return returns a blockpos shifted up 0.21 blocks from the entity's current position
     */
    public static BlockPos getTemperatureTickPos(LivingEntity target) {
        Entity entity = target.getRootVehicle();
        Vec3 pos = entity.position();
        final float offset = 0.21f;
        if (entity.mainSupportingBlockPos.isPresent()) {
            BlockPos blockPos = entity.mainSupportingBlockPos.get();
            BlockState blockState = entity.level().getBlockState(blockPos);
            return !blockState.is(BlockTags.FENCES) && !blockState.is(BlockTags.WALLS) && !(blockState.getBlock() instanceof FenceGateBlock)
                    ? blockPos.atY(Mth.floor(pos.y + offset))
                    : blockPos;
        } else {
            return new BlockPos(
                    Mth.floor(pos.x),
                    Mth.floor(pos.y + offset),
                    Mth.floor(pos.z)
            );
        }
    }

    private static void invokeEntityEvents(
            EnvironmentTickContext<? extends LivingEntity> context,
            HolderLookup<TemperatureSource> lookup
    ) {
        tickSoakingChange(
                context,
                LivingEntitySoakingTickEvents.ALLOW_SOAKING_UPDATE,
                LivingEntitySoakingTickEvents.GET_SOAKING_CHANGE,
                LivingEntitySoakingTickEvents.ALLOW_SOAKING_CHANGE
        );
        UpdateEvents.invokeAllWithContext(context, lookup);
    }

    private static void tickSoakingChange(
            EnvironmentTickContext<? extends LivingEntity> context,
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

    private LivingEntityTickUtil() {
    }
}