/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.mixin.client;

import com.github.thedeathlycow.thermoo.api.client.v1.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.client.HeartBarContextImpl;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
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
public abstract class GuiMountTemperatureMixin {
    @Shadow
    protected abstract LivingEntity getPlayerVehicleWithHealth();

    @Shadow
    protected abstract Player getCameraPlayer();

    @Shadow
    protected abstract int getVehicleMaxHearts(@Nullable LivingEntity entity);

    @Inject(
            method = "extractVehicleHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
                    ordinal = 0
            )
    )
    private void startHeartCapture(
            GuiGraphicsExtractor poseStack,
            CallbackInfo ci,
            @Local(name = "xo") int heartX,
            @Local(name = "yo") int heartY,
            @Share("thermoo_heart_positions") LocalRef<SequencedCollection<Vector2i>> heartPositionsRef
    ) {
        if (heartPositionsRef.get() == null) {
            heartPositionsRef.set(new ArrayList<>());
        }

        heartPositionsRef.get().add(new Vector2i(heartX, heartY));
    }

    @Inject(
            method = "extractVehicleHealth",
            at = @At("TAIL")
    )
    private void renderMountHealth(
            GuiGraphicsExtractor graphics,
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
