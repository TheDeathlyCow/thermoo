package com.github.thedeathlycow.thermoo.mixin;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentChangeResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerTemperatureEventMixin {

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
        Biome biome = world.getBiome(pos).value();
        int temperatureChange = EnvironmentController.INSTANCE.getLocalTemperatureChange(world, pos);

        EnvironmentChangeResult result = new EnvironmentChangeResult();
        if (temperatureChange < 0) {
            PlayerEnvironmentEvents.COLD_BIOME_TICK.invoker().onTemperatureChangeTick(
                    EnvironmentController.INSTANCE, player, biome, temperatureChange, result
            );
        } else if (temperatureChange > 0) {
            PlayerEnvironmentEvents.WARM_BIOME_TICK.invoker().onTemperatureChangeTick(
                    EnvironmentController.INSTANCE, player, biome, temperatureChange, result
            );
        } else {
            PlayerEnvironmentEvents.TEMPERATE_BIOME_TICK.invoker().onTemperatureChangeTick(
                    EnvironmentController.INSTANCE, player, biome
            );
        }

    }

}
