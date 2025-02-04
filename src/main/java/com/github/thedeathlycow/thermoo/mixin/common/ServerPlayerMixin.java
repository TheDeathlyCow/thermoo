package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.impl.environment.TickUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void afterTick(CallbackInfo ci) {
        TickUtil.tickPlayerTemperature((ServerPlayerEntity) (Object) this);
    }
}