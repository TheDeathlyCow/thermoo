package com.github.thedeathlycow.thermoo.mixin.neoforge;

import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSystem;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void afterTick(CallbackInfo ci) {
        TemperatureStatusSystem.doTick((LivingEntity) (Object) this);
    }
}