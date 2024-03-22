package com.github.thedeathlycow.thermoo.mixin.client.compat.overflowingbars.present;

import com.github.thedeathlycow.thermoo.api.client.HeartOverlayRenderEvent;
import com.github.thedeathlycow.thermoo.impl.client.HeartOverlayImpl;
import fuzs.overflowingbars.client.handler.HealthBarRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@Mixin(value = HealthBarRenderer.class, remap = false)
public class HealthBarRendererMixin {

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lfuzs/overflowingbars/client/handler/HealthBarRenderer;renderHeart(Lnet/minecraft/client/gui/DrawContext;Lfuzs/overflowingbars/client/handler/HealthBarRenderer$HeartType;IIZZZ)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER,
                    remap = true
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void captureHeartPosition(
            DrawContext guiGraphics,
            PlayerEntity player,
            int posX,
            int posY,
            int heartOffsetByRegen,
            float maxHealth,
            int currentHealth,
            int displayHealth,
            int currentAbsorptionHealth,
            boolean blink,
            CallbackInfo ci,
            boolean hardcore,
            int normalHearts,
            int maxAbsorptionHearts,
            int absorptionHearts,
            int currentHeart,
            int currentPosX,
            int currentPosY
    ) {
        HeartOverlayImpl.INSTANCE.setHeartPosition(currentHeart, currentPosX, currentPosY);
    }

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "TAIL"
            )
    )
    private void renderColdHeartOverlayBar(
            DrawContext guiGraphics,
            PlayerEntity player,
            int posX,
            int posY,
            int heartOffsetByRegen,
            float maxHealth,
            int currentHealth,
            int displayHealth,
            int currentAbsorptionHealth,
            boolean blink,
            CallbackInfo ci
    ) {
        HeartOverlayRenderEvent.AFTER_HEALTH_BAR.invoker()
                .render(
                        guiGraphics,
                        player,
                        HeartOverlayImpl.INSTANCE.getHeartPositions(),
                        displayHealth,
                        20
                );
        Arrays.fill(HeartOverlayImpl.INSTANCE.getHeartPositions(), null);
    }

}
