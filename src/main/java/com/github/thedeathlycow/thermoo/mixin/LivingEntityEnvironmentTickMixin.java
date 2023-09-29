package com.github.thedeathlycow.thermoo.mixin;

import com.github.thedeathlycow.thermoo.impl.LivingEntityEnvironmentTickImpl;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityEnvironmentTickMixin {

    @Unique
    private int thermoo_lastTickTemperature = 0;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;tick()V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void temperatureTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingEntityEnvironmentTickImpl.tick(entity, thermoo_lastTickTemperature);
        thermoo_lastTickTemperature = entity.thermoo$getTemperature();
    }

}
