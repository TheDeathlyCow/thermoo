package com.github.thedeathlycow.thermoo.mixin.client;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Shadow protected abstract int getHeartCount(@Nullable LivingEntity entity);

    @Inject(
            method = "renderMountHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V",
                    ordinal = 0
            )
    )
    private void captureMountHealth(
            DrawContext context,
            CallbackInfo ci,
            @Local(ordinal = 8) int heartX,
            @Local(ordinal = 4) int heartY,
            @Share("thermoo_heart_positions") LocalRef<List<Vector2i>> heartPositions
    ) {
        if (heartPositions.get() == null) {
            heartPositions.set(new ArrayList<>());
        }
        heartPositions.get().add(new Vector2i(heartX, heartY));
    }

    @Inject(
            method = "renderMountHealth",
            at = @At("TAIL")
    )
    private void renderMountHealth(
            DrawContext context,
            CallbackInfo ci,
            @Share("thermoo_heart_positions") LocalRef<List<Vector2i>> heartPositionsRef
    ) {
        List<Vector2i> heartPositions = heartPositionsRef.get();
        if (heartPositions == null) {
            return;
        }

        PlayerEntity player = this.getCameraPlayer();
        LivingEntity mount = this.getRiddenEntity();

        // this weirdness accounts for two vanilla bugs:
        // - MC-200102: last half heart is not displayed with an odd max health
        // - a second bug that only shows up to 3 rows of mount health
        int maxHealth = this.getHeartCount(mount) * 2;
        int health = Math.min(MathHelper.ceil(mount.getHealth()), maxHealth);

        StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR.invoker()
                .render(
                        context,
                        player,
                        mount,
                        Collections.unmodifiableList(heartPositions),
                        health,
                        maxHealth
                );
    }
}
