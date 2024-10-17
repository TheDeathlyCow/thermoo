package com.github.thedeathlycow.thermoo.mixin.client;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.client.HeartOverlayTracker;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
                    ordinal = 0
            )
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
            @Local(ordinal = 10) int index,
            @Local(ordinal = 13) int heartX,
            @Local(ordinal = 14) int heartY,
            @Share("thermoo_tracker") LocalRef<HeartOverlayTracker> tracker
    ) {
        if (tracker.get() == null) {
            tracker.set(new HeartOverlayTracker());
        }

        tracker.get().setHeartPosition(index, heartX, heartY);
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
            CallbackInfo ci,
            @Share("thermoo_tracker") LocalRef<HeartOverlayTracker> tracker
    ) {
        Vector2i[] heartPositions = tracker.get().getHeartPositions();
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
    }

}
