package com.github.thedeathlycow.thermoo.mixin.client.compat.colorfulhearts.present;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.ThermooIntegrations;
import com.github.thedeathlycow.thermoo.impl.client.HeartOverlayImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import terrails.colorfulhearts.api.event.HeartRenderEvent;
import terrails.colorfulhearts.api.heart.drawing.Heart;
import terrails.colorfulhearts.api.heart.drawing.OverlayHeart;
import terrails.colorfulhearts.render.HeartRenderer;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@Mixin(value = HeartRenderer.class, remap = false)
public class HeartRendererMixin {

    @Inject(method = "renderPlayerHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lterrails/colorfulhearts/api/heart/drawing/Heart;draw(Lnet/minecraft/client/gui/DrawContext;IIZZZ)V",
                    ordinal = 0,
                    remap = true,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void captureHeartPositions(
            DrawContext guiGraphics,
            PlayerEntity player,
            int x, int y,
            int maxHealth, int currentHealth,
            int displayHealth, int absorption,
            boolean renderHighlight,
            CallbackInfo ci,
            long ticks,
            int healthHearts, int displayHealthHearts,
            boolean hardcore,
            int regenIndex,
            OverlayHeart heartType,
            HeartRenderEvent.Pre event,
            int index,
            Heart heart,
            int xPos, int yPos,
            boolean highlightHeart
    ) {
        if (ThermooIntegrations.isModLoaded(ThermooIntegrations.OVERFLOWING_BARS_ID)) {
            return;
        }
        HeartOverlayImpl.INSTANCE.setHeartPosition(index, xPos, yPos);
    }

    @Inject(
            method = "renderPlayerHearts",
            at = @At(
                    value = "TAIL",
                    shift = At.Shift.BEFORE
            )
    )
    private void drawColdHeartOverlayBar(
            DrawContext drawContext,
            PlayerEntity player,
            int x, int y,
            int maxHealth, int currentHealth, int displayHealth, int absorption,
            boolean renderHighlight,
            CallbackInfo ci
    ) {
        if (ThermooIntegrations.isModLoaded(ThermooIntegrations.OVERFLOWING_BARS_ID)) {
            return;
        }
        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.invoker()
                .render(
                        drawContext,
                        player,
                        HeartOverlayImpl.INSTANCE.getHeartPositions(),
                        displayHealth,
                        20
                );
        Arrays.fill(HeartOverlayImpl.INSTANCE.getHeartPositions(), null);
    }

}
