package com.github.thedeathlycow.thermoo.mixin.client;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.client.HeartOverlayTracker;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For the mount health bar. For the player health bar see {@link InGameHudPlayerTemperatureMixin}
 */
@Mixin(InGameHud.class)
@Debug(export = true)
public abstract class InGameHudMountTemperatureMixin {
    @Shadow
    protected abstract LivingEntity getRiddenEntity();

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Inject(
            method = "renderMountHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 0
            )
    )
    private void captureMountHealth(
            DrawContext context,
            CallbackInfo ci,
            @Local(ordinal = 7) int index,
            @Local(ordinal = 8) int heartX,
            @Local(ordinal = 4) int heartY,
            @Share("thermoo_tracker") LocalRef<HeartOverlayTracker> tracker
    ) {
        if (tracker.get() == null) {
            tracker.set(new HeartOverlayTracker());
        }
        tracker.get().setHeartPosition(index, heartX, heartY);
    }

    @Inject(
            method = "renderMountHealth",
            at = @At("TAIL")
    )
    private void renderMountHealth(
            DrawContext context,
            CallbackInfo ci,
            @Share("thermoo_tracker") LocalRef<HeartOverlayTracker> tracker
    ) {
        Vector2i[] heartPositions = tracker.get().getHeartPositions();

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
    }
}
