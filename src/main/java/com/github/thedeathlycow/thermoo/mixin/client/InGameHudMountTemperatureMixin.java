package com.github.thedeathlycow.thermoo.mixin.client;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.client.HeartOverlayTracker;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For the mount health bar. For the player health bar see {@link InGameHudPlayerTemperatureMixin}
 */
@Mixin(Gui.class)
@Debug(export = true)
public abstract class InGameHudMountTemperatureMixin {
    @Shadow
    protected abstract LivingEntity getPlayerVehicleWithHealth();

    @Shadow
    protected abstract Player getCameraPlayer();

    @Shadow protected abstract int getVehicleMaxHearts(@Nullable LivingEntity entity);

    @Inject(
            method = "renderVehicleHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V",
                    ordinal = 0
            )
    )
    private void captureMountHealth(
            GuiGraphics context,
            CallbackInfo ci,
            @Local(ordinal = 8) int heartX,
            @Local(ordinal = 4) int heartY,
            @Share("thermoo_index") LocalIntRef index,
            @Share("thermoo_tracker") LocalRef<HeartOverlayTracker> tracker
    ) {
        if (tracker.get() == null) {
            tracker.set(new HeartOverlayTracker());
        }
        int indexValue = index.get();
        tracker.get().addHeartPosition(indexValue, heartX, heartY);
        index.set(indexValue + 1);
    }

    @Inject(
            method = "renderVehicleHealth",
            at = @At("TAIL")
    )
    private void renderMountHealth(
            GuiGraphics context,
            CallbackInfo ci,
            @Share("thermoo_tracker") LocalRef<HeartOverlayTracker> tracker
    ) {
        Vector2i[] heartPositions = tracker.get().getHeartPositions();

        Player player = this.getCameraPlayer();
        LivingEntity mount = this.getPlayerVehicleWithHealth();

        // this weirdness accounts for two vanilla bugs:
        // - MC-200102: last half heart is not displayed with an odd max health
        // - a second bug that only shows up to 3 rows of mount health
        int maxHealth = this.getVehicleMaxHearts(mount) * 2;
        int health = Math.min(Mth.ceil(mount.getHealth()), maxHealth);

        StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR.invoker()
                .render(
                        context,
                        player,
                        mount,
                        heartPositions,
                        health,
                        maxHealth
                );
    }
}
