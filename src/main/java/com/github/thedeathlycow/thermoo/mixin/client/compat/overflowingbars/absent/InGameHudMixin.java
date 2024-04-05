package com.github.thedeathlycow.thermoo.mixin.client.compat.overflowingbars.absent;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.client.HeartOverlayImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow protected abstract LivingEntity getRiddenEntity();

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Unique
    private int scorchful$mountIndex = 0;

    // player health bar

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

    // mount health bar
    @WrapOperation(
            method = "renderMountHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                    ordinal = 0
            )
    )
    private void captureMountHealth(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        HeartOverlayImpl.INSTANCE.setHeartPosition(scorchful$mountIndex, x, y);
        original.call(instance, texture, x, y, u, v, width, height);
        scorchful$mountIndex++;
    }

    @Inject(
            method = "renderMountHealth",
            at = @At("TAIL")
    )
    private void renderMountHealth(DrawContext context, CallbackInfo ci) {
        Vector2i[] heartPositions = HeartOverlayImpl.INSTANCE.getHeartPositions();

        PlayerEntity player = this.getCameraPlayer();
        LivingEntity mount = this.getRiddenEntity();
        float health = mount.getHealth();
        float maxHealth = mount.getMaxHealth();

        int displayHealth = Math.min(MathHelper.ceil(health), heartPositions.length);
        int maxDisplayHealth = Math.min(MathHelper.ceil(maxHealth), heartPositions.length);

        StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR.invoker()
                .render(
                        context,
                        player,
                        mount,
                        heartPositions,
                        displayHealth,
                        maxDisplayHealth
                );
        Arrays.fill(HeartOverlayImpl.INSTANCE.getHeartPositions(), null);
        scorchful$mountIndex = 0;
    }

}
