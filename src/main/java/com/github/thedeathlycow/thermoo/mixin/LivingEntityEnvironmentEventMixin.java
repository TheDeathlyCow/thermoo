package com.github.thedeathlycow.thermoo.mixin;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityEnvironmentEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.InitialSoakChangeResult;
import com.github.thedeathlycow.thermoo.api.temperature.event.InitialTemperatureChangeResult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityEnvironmentEventMixin {

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;tick()V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void onTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        World world = entity.getWorld();

        if (world.isClient() || entity.isSpectator()) {
            return;
        }

        EnvironmentController controller = EnvironmentManager.INSTANCE.getController();

        int heatSourceTemperatureChange = controller.getWarmthFromHeatSources(entity, world, entity.getBlockPos());

        if (heatSourceTemperatureChange != 0) {
            var heatedLocationResult = new InitialTemperatureChangeResult(entity, heatSourceTemperatureChange, HeatingModes.PASSIVE);
            LivingEntityEnvironmentEvents.TICK_IN_HEATED_LOCATION.invoker().onTemperatureChange(
                    controller, entity, heatedLocationResult
            );
            heatedLocationResult.onEventComplete();
        }

        var heatFxResult = new InitialTemperatureChangeResult(entity, controller.getOnFireWarmthRate(entity), HeatingModes.ACTIVE);
        LivingEntityEnvironmentEvents.TICK_HEAT_EFFECTS.invoker().onTemperatureChange(
                controller, entity, heatFxResult
        );
        heatFxResult.onEventComplete();

        int soakChange = controller.getSoakChange(entity);
        if (soakChange != 0) {
            var wetResult = new InitialSoakChangeResult(entity, soakChange);
            LivingEntityEnvironmentEvents.TICK_IN_WET_LOCATION.invoker().onSoakChange(
                    controller, entity, wetResult
            );
            wetResult.onEventComplete();
        }
    }

}
