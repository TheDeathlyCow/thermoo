package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.impl.LivingEntityEnvironmentTickImpl;
import com.github.thedeathlycow.thermoo.impl.LivingEntityTickUtil;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityEnvironmentTickMixin {
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void temperatureTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingEntityEnvironmentTickImpl.tick(entity);
        LivingEntityTickUtil.tick(entity);
    }
}
