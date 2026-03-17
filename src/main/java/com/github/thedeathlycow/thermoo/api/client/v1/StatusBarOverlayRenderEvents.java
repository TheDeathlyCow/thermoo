package com.github.thedeathlycow.thermoo.api.client.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

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
            callbacks -> (context, player, heartBarContext) -> {
                for (RenderHealthBarCallback callback : callbacks) {
                    callback.render(context, player, heartBarContext);
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
            callbacks -> (context, player, mount, heartBarContext) -> {
                for (RenderMountHealthBarCallback callback : callbacks) {
                    callback.render(
                            context,
                            player,
                            mount,
                            heartBarContext
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
         * @param graphics        Graphical draw context for the HUD
         * @param player          The player rendering hearts for
         * @param heartBarContext Data associated with the player heart bar.
         */
        void render(
                GuiGraphics graphics,
                Player player,
                HeartBarContext heartBarContext
        );
    }

    @FunctionalInterface
    public interface RenderMountHealthBarCallback {
        /**
         * @param graphics        Graphical draw context for the HUD
         * @param player          The main player
         * @param mount           The animal the player is riding (ex: pig, horse, camel)
         * @param heartBarContext Data associated with the mount heart bar.
         */
        void render(
                GuiGraphics graphics,
                Player player,
                LivingEntity mount,
                HeartBarContext heartBarContext
        );
    }

    private StatusBarOverlayRenderEvents() {

    }
}
