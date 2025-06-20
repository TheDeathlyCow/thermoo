package com.github.thedeathlycow.thermoo.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Vector2i;

import java.util.List;

/**
 * Event for rendering temperature overlays on status bar.
 */
@Environment(EnvType.CLIENT)
public final class StatusBarOverlayRenderEvents {
    /**
     * Invoked after the player health bar is drawn. Does not include information on the Absorption bar.
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

    /**
     * Invoked after the players mount health is drawn.
     * <p>
     * Is not integrated with Colorful Hearts or Overflowing Bars by default, however these mods do not override the mount
     * health.
     * <p>
     * Note that indexes are backwards from the regular health: index 0 is the heart on the far RIGHT of the screen.
     * Adjust half-hearts accordingly.
     */
    public static final Event<RenderMountHealthBarCallback> AFTER_MOUNT_HEALTH_BAR = EventFactory.createArrayBacked(
            RenderMountHealthBarCallback.class,
            callbacks -> (context, player, mount, mountHeartPositions, displayMountHealth, maxDisplayMountHealth) -> {
                for (RenderMountHealthBarCallback callback : callbacks) {
                    callback.render(
                            context,
                            player, mount,
                            mountHeartPositions,
                            displayMountHealth, maxDisplayMountHealth
                    );
                }
            }
    );

    @FunctionalInterface
    public interface RenderHealthBarCallback {
        /**
         * Note that {@code displayHealth} and {@code maxDisplayHealth} are not always the same as health and max
         * health. Mods that override the health bar rendering like Colorful Hearts may change these values.
         *
         * @param context          DrawContext for the HUD
         * @param player           The player rendering hearts for
         * @param heartPositions   A list of heart positions where they were rendered on the HUD, ordered from
         *                         left-to-right, bottom to top.
         * @param displayHealth    How many half hearts are to be displayed
         * @param maxDisplayHealth The maximum number of half hearts to be displayed
         */
        void render(
                DrawContext context,
                PlayerEntity player,
                List<Vector2i> heartPositions,
                int displayHealth,
                int maxDisplayHealth
        );
    }

    @FunctionalInterface
    public interface RenderMountHealthBarCallback {
        /**
         * @param context               Draw context
         * @param player                The main player
         * @param mount                 The animal the player is riding (ex: pig, horse, camel)
         * @param mountHeartPositions   A list of heart positions where they were rendered on the HUD, ordered from
         *                              right-to-left, bottom to top.
         * @param displayMountHealth    How many half hearts are to be displayed
         * @param maxDisplayMountHealth The maximum number of half hearts to be displayed
         */
        void render(
                DrawContext context,
                PlayerEntity player,
                LivingEntity mount,
                List<Vector2i> mountHeartPositions,
                int displayMountHealth,
                int maxDisplayMountHealth
        );
    }

    private StatusBarOverlayRenderEvents() {

    }
}
