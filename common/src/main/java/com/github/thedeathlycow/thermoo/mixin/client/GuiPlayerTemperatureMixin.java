/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
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
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.SequencedCollection;

@Mixin(Gui.class)
public abstract class GuiPlayerTemperatureMixin {
    @Inject(
            method = "extractHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;extractHeart(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
                    ordinal = 0
            )
    )
    private void captureHeartPositions(
            GuiGraphicsExtractor graphics,
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
            @Local(name = "containerIndex") int index,
            @Local(name = "xo") int heartX,
            @Local(name = "yo") int heartY,
            @Share("thermoo_heart_positions") LocalRef<SequencedCollection<Vector2i>> heartPositionsRef
    ) {
        if (heartPositionsRef.get() == null) {
            heartPositionsRef.set(new ArrayDeque<>());
        }
        heartPositionsRef.get().addFirst(new Vector2i(heartX, heartY));
    }

    @Inject(
            method = "extractHearts",
            at = @At(
                    value = "TAIL"
            )
    )
    private void drawHeartOverlayBar(
            GuiGraphicsExtractor graphics,
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
            @Share("thermoo_heart_positions") LocalRef<SequencedCollection<Vector2i>> heartPositionsRef
    ) {
        SequencedCollection<Vector2i> heartPositions = heartPositionsRef.get();
        if (heartPositions == null) {
            return;
        }

        int maxDisplayHealth = Mth.ceil(maxHealth);

        var heartBarContext = new HeartBarContextImpl(
                Collections.unmodifiableSequencedCollection(heartPositions),
                health,
                maxDisplayHealth
        );

        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.invoker().render(graphics, player, heartBarContext);
    }
}
