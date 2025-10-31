package com.github.thedeathlycow.thermoo.mixin.client;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.client.HeartBarContextImpl;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.SequencedCollection;

/**
 * For the mount health bar. For the player health bar see {@link GuiPlayerTemperatureMixin}
 */
@Mixin(Gui.class)
@Debug(export = true)
public abstract class GuiMountTemperatureMixin {
    @Shadow
    protected abstract LivingEntity getPlayerVehicleWithHealth();

    @Shadow
    protected abstract Player getCameraPlayer();

    @Shadow
    protected abstract int getVehicleMaxHearts(@Nullable LivingEntity entity);

    @Inject(
            method = "renderVehicleHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIII)V",
                    ordinal = 0
            )
    )
    private void startHeartCapture(
            GuiGraphics poseStack,
            CallbackInfo ci,
            @Local(ordinal = 8) int heartX,
            @Local(ordinal = 4) int heartY,
            @Share("thermoo_heart_positions") LocalRef<SequencedCollection<Vector2i>> heartPositionsRef
    ) {
        if (heartPositionsRef.get() == null) {
            heartPositionsRef.set(new ArrayList<>());
        }

        heartPositionsRef.get().add(new Vector2i(heartX, heartY));
    }

    @Inject(
            method = "renderVehicleHealth",
            at = @At("TAIL")
    )
    private void renderMountHealth(
            GuiGraphics graphics,
            CallbackInfo ci,
            @Share("thermoo_heart_positions") LocalRef<SequencedCollection<Vector2i>> heartPositionsRef
    ) {
        SequencedCollection<Vector2i> heartPositions = heartPositionsRef.get();
        if (heartPositions == null) {
            return;
        }

        Player player = this.getCameraPlayer();
        LivingEntity mount = this.getPlayerVehicleWithHealth();

        // this weirdness accounts for two vanilla bugs:
        // - MC-200102: last half heart is not displayed with an odd max health
        // - a second bug that only shows up to 3 rows of mount health
        int maxHealth = this.getVehicleMaxHearts(mount) * 2;
        int health = Math.min(Mth.ceil(mount.getHealth()), maxHealth);

        var heartBarContext = new HeartBarContextImpl(
                Collections.unmodifiableSequencedCollection(heartPositions),
                health,
                maxHealth
        );

        StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR.invoker().render(graphics, player, mount, heartBarContext);
    }
}
