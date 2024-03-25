package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerTemperatureTickMixin {

    @Shadow protected abstract void collideWithEntity(Entity entity);

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;tick()V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void onTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getWorld();

        if (world.isClient || player.isSpectator()) {
            return;
        }

        BlockPos pos = player.getBlockPos();
        var controller = EnvironmentManager.INSTANCE.getController();
        int temperatureChange = controller.getLocalTemperatureChange(world, pos);

        boolean canApplyChange = temperatureChange != 0 &&
                PlayerEnvironmentEvents.CAN_APPLY_PASSIVE_TEMPERATURE_CHANGE
                        .invoker()
                        .canApplyChange(temperatureChange, player);

        if (canApplyChange) {
            temperatureChange = controller.getEnvironmentTemperatureForPlayer(player, temperatureChange);
            player.thermoo$addTemperature(temperatureChange, HeatingModes.PASSIVE);
        }

    }

}
