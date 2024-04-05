package com.github.thedeathlycow.thermoo.mixin.client;

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

import java.util.Arrays;

/**
 * For the mount health bar. For the player health bar see {@link com.github.thedeathlycow.thermoo.mixin.client.compat.overflowingbars.absent.InGameHudMixin}
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    protected abstract LivingEntity getRiddenEntity();

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Unique
    private int scorchful$mountIndex = 0;

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
