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

package com.github.thedeathlycow.thermoo.gametest.init;

import com.github.thedeathlycow.thermoo.api.client.v1.HeartBarContext;
import com.github.thedeathlycow.thermoo.api.client.v1.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class ThermooTestModClient {
    public static final Identifier HEART_OVERLAY_TEXTURE = Thermoo.id("textures/gui/fire_heart_overlay.png");

    private static final int TEXTURE_WIDTH = 18;
    private static final int TEXTURE_HEIGHT = 30;

    public static void onInitializeClient() {
        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.register(ThermooTestModClient::renderFireHeartBar);
        StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR.register(ThermooTestModClient::renderMountFireHeartBar);
    }

    public static void renderMountFireHeartBar(
            GuiGraphicsExtractor graphics,
            Player player,
            LivingEntity mount,
            HeartBarContext heartBarContext
    ) {
        final int fireHalfHearts = getNumFireHalfHearts(mount, heartBarContext.positions().size());
        final int fireHearts = getNumFireHearts(fireHalfHearts);
        final boolean drawHalfHeartAtEnd = fireHalfHearts % 2 != 0;

        int heartsRendered = 0;

        for (Vector2i position : heartBarContext.positions()) {
            if (heartsRendered >= fireHearts) {
                break;
            }

            int x = position.x();
            int y = position.y() - 1;
            boolean isHalfHeart = drawHalfHeartAtEnd && heartsRendered == fireHearts - 1;

            if (isHalfHeart) {
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        HEART_OVERLAY_TEXTURE,
                        x + 4, y,
                        4, 0,
                        5, 10,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT
                );
            } else {
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        HEART_OVERLAY_TEXTURE,
                        x, y,
                        0, 0,
                        9, 10,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT
                );
            }

            heartsRendered++;
        }
    }

    public static void renderFireHeartBar(
            GuiGraphicsExtractor graphics,
            Player player,
            HeartBarContext heartBarContext
    ) {
        final int fireHalfHearts = getNumFireHalfHearts(player, heartBarContext.positions().size());
        final int fireHearts = getNumFireHearts(fireHalfHearts);
        final boolean drawHalfHeartAtEnd = fireHalfHearts % 2 != 0;

        int heartsRendered = 0;

        for (Vector2i position : heartBarContext.positions()) {
            if (heartsRendered >= fireHearts) {
                break;
            }

            int x = position.x();
            int y = position.y() - 1;
            int u = drawHalfHeartAtEnd && heartsRendered == fireHearts - 1 ? 9 : 0;

            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    HEART_OVERLAY_TEXTURE,
                    x, y,
                    u, 0,
                    9, 10,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );

            heartsRendered++;
        }
    }

    private static int getNumFireHalfHearts(@NotNull LivingEntity player, int maxDisplayHealth) {
        float overheatProgress = player.thermoo$getTemperatureScale();
        if (overheatProgress <= 0f) {
            return 0;
        }
        return Math.round(overheatProgress * maxDisplayHealth * 2);
    }

    private static int getNumFireHearts(int halfHearts) {
        return Mth.ceil(halfHearts / 2.0f);
    }

    private static boolean isHalfHeart(int index, int size) {
        return index >= size && index % 2 != 0;
    }

    private ThermooTestModClient() {

    }
}
