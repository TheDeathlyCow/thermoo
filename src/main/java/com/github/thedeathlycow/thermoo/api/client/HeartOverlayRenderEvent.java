package com.github.thedeathlycow.thermoo.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * Event for rendering temperature overlays on the health bar. Invoked after the health bar has been rendered. This is
 * not rendered for the absorption bar.
 * <p>
 * Supports Colourful Hearts and Overflowing Bars natively.
 * <p>
 * Custom heart types, like Frozen Hearts, should be handled separately.
 */
@Environment(EnvType.CLIENT)
public class HeartOverlayRenderEvent {

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
         * @param context         DrawContext for the HUD
         * @param player          The player rendering hearts for
         * @param heartPositions  An array of heart positions on the HUD
         * @param displayHealth   How many half hearts are to be displayed
         * @param maxDisplayHeath The maximum number of half hearts to be displayed
         */
        void render(
                DrawContext context,
                PlayerEntity player,
                Vector2i[] heartPositions,
                int displayHealth,
                int maxDisplayHeath
        );

    }

}
