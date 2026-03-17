package com.github.thedeathlycow.thermoo.mixin.client;

import com.github.thedeathlycow.thermoo.api.client.v1.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.client.HeartBarContextImpl;
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

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.SequencedCollection;

@Mixin(Gui.class)
public abstract class GuiPlayerTemperatureMixin {

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
                    ordinal = 0
            )
    )
    private void captureHeartPositions(
            GuiGraphics graphics,
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
            @Share("thermoo_heart_positions") LocalRef<SequencedCollection<Vector2i>> heartPositionsRef
    ) {
        if (heartPositionsRef.get() == null) {
            heartPositionsRef.set(new ArrayDeque<>());
        }
        heartPositionsRef.get().addFirst(new Vector2i(heartX, heartY));
    }

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "TAIL"
            )
    )
    private void drawHeartOverlayBar(
            GuiGraphics graphics,
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
