package com.github.thedeathlycow.thermoo.mixin;

import com.github.thedeathlycow.thermoo.api.ThermooTags;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class HotFloorMixin {

    @Inject(
            method = "onSteppedOn",
            at = @At("HEAD")
    )
    private void heatEntitiesFromHotFloor(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {

        if (world.isClient || !state.isIn(ThermooTags.HOT_FLOOR)) {
            return;
        }

        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.isSpectator() || livingEntity.isRemoved() || livingEntity.isDead()) {
                return;
            }

            livingEntity.thermoo$addTemperature(
                    EnvironmentManager.INSTANCE.getController().getHotFloorWarmth(state),
                    HeatingModes.ACTIVE
            );
        }



    }

}
