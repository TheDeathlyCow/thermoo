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

package com.github.thedeathlycow.thermoo.api.client.v1;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.commons.event.Event;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * Event for rendering temperature overlays on status bar.
 */
public final class StatusBarOverlayRenderEvents {
    /**
     * Invoked after the player health bar is drawn. Does not include information on the Absorption bar.
     * <p>
     * Custom heart types, like Frozen Hearts, should be handled separately.
     */
    public static final Event<Identifier, RenderHealthBarCallback> AFTER_HEALTH_BAR = Thermoo.EVENT_MANAGER.create(
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
    public static final Event<Identifier, RenderMountHealthBarCallback> AFTER_MOUNT_HEALTH_BAR = Thermoo.EVENT_MANAGER.create(
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
                GuiGraphicsExtractor graphics,
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
                GuiGraphicsExtractor graphics,
                Player player,
                LivingEntity mount,
                HeartBarContext heartBarContext
        );
    }

    private StatusBarOverlayRenderEvents() {

    }
}
