package com.github.thedeathlycow.thermoo.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import org.joml.Vector2f;

/**
 * Event for rendering temperature overlays on the health bar. Invoked after the health bar has been rendered.
 * <p>
 * Supports Colourful Hearts and Overflowing Bars natively.
 * <p>
 * Custom heart types, like Frozen Hearts, should be handled separately.
 */
@Environment(EnvType.CLIENT)
@SuppressWarnings("UNUSED")
public class HeartOverlayRenderEvent {

    public static final Event<RenderHeartBarCallback> EVENT = EventFactory.createArrayBacked(
            RenderHeartBarCallback.class,
            callbacks -> (context, player, heartPositions, maxHalfHearts) -> {
                for (RenderHeartBarCallback callback : callbacks) {
                    callback.render(context, player, heartPositions, maxHalfHearts);
                }
            }
    );

    @FunctionalInterface
    public interface RenderHeartBarCallback {

        /**
         * @param context        DrawContext for the HUD
         * @param player         The player rendering hearts for
         * @param heartPositions An array of heart positions on the HUD
         * @param maxHalfHearts  The maximum number of half hearts the player could have, including empty hearts.
         *                       This is NOT always the same as the max health, e.g. with a health bar overriding mod
         *                       like Colourful Hearts it is at most 20.
         */
        void render(DrawContext context, ClientPlayerEntity player, Vector2f[] heartPositions, float maxHalfHearts);

    }

}
