package com.github.thedeathlycow.thermoo.mixin;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.event.InitialTemperatureChangeResult;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
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
        var controller = EnvironmentManager.INSTANCE.getController();
        int temperatureChange = controller.getLocalTemperatureChange(world, pos);

        var result = new InitialTemperatureChangeResult(player, temperatureChange, HeatingModes.PASSIVE);
        if (temperatureChange != 0) {
            PlayerEnvironmentEvents.TICK_BIOME_TEMPERATURE_CHANGE.invoker().onBiomeTemperatureChange(
                    controller, player, biome, result
            );
            result.onEventComplete();
        }

    }

}
