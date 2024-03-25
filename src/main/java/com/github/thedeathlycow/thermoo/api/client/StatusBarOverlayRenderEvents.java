package com.github.thedeathlycow.thermoo.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Vector2i;

/**
 * Event for rendering temperature overlays on status bar.
 */
@Environment(EnvType.CLIENT)
public class StatusBarOverlayRenderEvents {

    /**
     * Invoked after the player health bar is drawn. Does not include information on the Absorption bar.
     * <p>
     * Integrates with Colorful Hearts and Overflowing Bars for now - but this integration will be removed in the
     * future.
     * <p>
     * Custom heart types, like Frozen Hearts, should be handled separately.
     */
    public static final Event<RenderHealthBarCallback> AFTER_HEALTH_BAR = EventFactory.createArrayBacked(
            RenderHealthBarCallback.class,
            callbacks -> (context, player, heartPositions, displayHealth, maxDisplayHeath) -> {
                for (RenderHealthBarCallback callback : callbacks) {
                    callback.render(context, player, heartPositions, displayHealth, maxDisplayHeath);
                }
            }
    );

    @FunctionalInterface
    public interface RenderHealthBarCallback {

        /**
         * Note that {@code displayHealth} and {@code maxDisplayHealth} are not always the same as health and max health. Mods that
         * override the health bar rendering like Colorful Hearts may change these values.
         *
         * @param context          DrawContext for the HUD
         * @param player           The player rendering hearts for
         * @param heartPositions   An array of heart positions on the HUD. Elements may be null, indicating that a heart
         *                         should not be rendered at this index.
         * @param displayHealth    How many half hearts are to be displayed
         * @param maxDisplayHealth The maximum number of half hearts to be displayed
         */
        void render(
                DrawContext context,
                PlayerEntity player,
                Vector2i[] heartPositions,
                int displayHealth,
                int maxDisplayHealth
        );

    }

}
