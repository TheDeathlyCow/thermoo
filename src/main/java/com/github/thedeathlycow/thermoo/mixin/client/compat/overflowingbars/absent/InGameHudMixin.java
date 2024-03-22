package com.github.thedeathlycow.thermoo.mixin.client.compat.overflowingbars.absent;

import com.github.thedeathlycow.thermoo.api.client.HeartOverlayRenderEvent;
import com.github.thedeathlycow.thermoo.impl.client.HeartOverlayImpl;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(
            method = "renderHealthBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIIZZ)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void captureHeartPositions(
            DrawContext context,
            PlayerEntity player,
            int x, int y,
            int lines,
            int regeneratingHeartIndex,
            float maxHealth,
            int lastHealth,
            int health,
            int absorption,
            boolean blinking,
            CallbackInfo ci,
            // local captures
            InGameHud.HeartType heartType,
            int i, int j, int k, int l,
            int m, // index of heart
            int n, int o,
            int p, int q // position of heart to capture
    ) {
        HeartOverlayImpl.INSTANCE.setHeartPosition(m, p, q);
    }

    @Inject(
            method = "renderHealthBar",
            at = @At(
                    value = "TAIL"
            )
    )
    private void drawHeartOverlayBar(
            DrawContext context,
            PlayerEntity player,
            int x, int y,
            int lines,
            int regeneratingHeartIndex,
            float maxHealth,
            int lastHealth,
            int health,
            int absorption,
            boolean blinking,
            CallbackInfo ci
    ) {

        Vector2i[] heartPositions = HeartOverlayImpl.INSTANCE.getHeartPositions();
        int displayHealth = Math.min(health, heartPositions.length);
        int maxDisplayHealth = Math.min(MathHelper.ceil(maxHealth), heartPositions.length);

        HeartOverlayRenderEvent.AFTER_HEALTH_BAR.invoker()
                .render(
                        context,
                        player,
                        heartPositions,
                        displayHealth,
                        maxDisplayHealth
                );
    }

}
