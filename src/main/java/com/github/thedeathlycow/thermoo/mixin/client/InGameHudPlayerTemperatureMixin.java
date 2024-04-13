package com.github.thedeathlycow.thermoo.mixin.client;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
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

import java.util.Arrays;

@Mixin(InGameHud.class)
public abstract class InGameHudPlayerTemperatureMixin {

    @Inject(
            method = "renderHealthBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V",
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
            InGameHud.HeartType heartType,
            boolean bl,
            int displayHearts,
            int displayAbsorption,
            int displayHalfHearts,
            int index,
            int lineY, int lineX,
            int heartX, int heartY
    ) {
        HeartOverlayImpl.INSTANCE.setHeartPosition(index, heartX, heartY);
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

        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.invoker()
                .render(
                        context,
                        player,
                        heartPositions,
                        displayHealth,
                        maxDisplayHealth
                );
        Arrays.fill(HeartOverlayImpl.INSTANCE.getHeartPositions(), null);
    }

}
