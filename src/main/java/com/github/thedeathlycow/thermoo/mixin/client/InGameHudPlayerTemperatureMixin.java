package com.github.thedeathlycow.thermoo.mixin.client;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.client.HeartOverlayTracker;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class InGameHudPlayerTemperatureMixin {

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
                    ordinal = 0
            )
    )
    private void captureHeartPositions(
            GuiGraphics context,
            Player player,
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

        tracker.get().addHeartPosition(index, heartX, heartY);
    }

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "TAIL"
            )
    )
    private void drawHeartOverlayBar(
            GuiGraphics context,
            Player player,
            int x, int y,
            int lines,
            int regeneratingHeartIndex,
            float maxHealth,
            int lastHealth,
            int health,
            int absorption,
            boolean blinking,
            CallbackInfo ci,
            @Share("thermoo_tracker") LocalRef<HeartOverlayTracker> trackerRef
    ) {
        HeartOverlayTracker tracker = trackerRef.get();
        if (tracker == null) {
            return;
        }

        Vector2i[] heartPositions = tracker.getHeartPositions();
        int maxDisplayHealth = Mth.ceil(maxHealth);

        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.invoker()
                .render(
                        context,
                        player,
                        heartPositions,
                        health,
                        maxDisplayHealth
                );
    }
}
