package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class HotFloorMixin {

    @Inject(
            method = "stepOn",
            at = @At("HEAD")
    )
    private void heatEntitiesFromHotFloor(Level world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {

        if (world.isClientSide) {
            return;
        }

        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.isSpectator() || livingEntity.isRemoved() || livingEntity.isDeadOrDying()) {
                return;
            }

            int temperatureChange = EnvironmentManager.INSTANCE.getController().getFloorTemperature(
                    livingEntity,
                    world,
                    state,
                    pos
            );

            if (temperatureChange != 0) {
                livingEntity.thermoo$addTemperature(
                        temperatureChange,
                        HeatingModes.ACTIVE
                );
            }
        }
    }

}
