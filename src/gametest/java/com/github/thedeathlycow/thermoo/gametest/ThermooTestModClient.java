package com.github.thedeathlycow.thermoo.gametest;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

public class ThermooTestModClient implements ClientModInitializer {

    public static final Identifier HEART_OVERLAY_TEXTURE = Thermoo.id("textures/gui/fire_heart_overlay.png");

    private static final int TEXTURE_WIDTH = 18;
    private static final int TEXTURE_HEIGHT = 30;

    @Override
    public void onInitializeClient() {
        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.register(ThermooTestModClient::renderFireHeartBar);
        StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR.register(ThermooTestModClient::renderMountFireHeartBar);
    }

    public static void renderMountFireHeartBar(
            DrawContext context,
            PlayerEntity player,
            LivingEntity mount,
            List<Vector2i> heartPositions,
            int displayHealth,
            int maxDisplayHealth
    ) {
        int fireHeartPoints = getNumFirePoints(mount, maxDisplayHealth);
        int fireHearts = getNumFireHeartsFromPoints(fireHeartPoints);

        for (int m = 0; m < fireHearts && m < heartPositions.size(); m++) {
            Vector2i heartPos = heartPositions.get(m);

            // is half heart if this is the last heart being rendered and we have an odd
            // number of frozen health points
            int x = heartPos.x;
            int y = heartPos.y - 1;
            boolean isHalfHeart = m + 1 >= fireHearts && (fireHeartPoints & 1) == 1; // is odd check;

            if (isHalfHeart) {
                context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        HEART_OVERLAY_TEXTURE,
                        x + 4, y,
                        4, 0,
                        5, 10,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT
                );
            } else {
                context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        HEART_OVERLAY_TEXTURE,
                        x, y,
                        0, 0,
                        9, 10,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT
                );
            }
        }
    }

    public static void renderFireHeartBar(
            DrawContext context,
            PlayerEntity player,
            List<Vector2i> heartPositions,
            int displayHealth,
            int maxDisplayHealth
    ) {
        int fireHeartPoints = getNumFirePoints(player, maxDisplayHealth);
        int fireHearts = getNumFireHeartsFromPoints(fireHeartPoints);

        for (int m = 0; m < fireHearts && m < heartPositions.size(); m++) {
            Vector2i heartPos = heartPositions.get(m);

            // is half heart if this is the last heart being rendered and we have an odd
            // number of frozen health points
            int x = heartPos.x;
            int y = heartPos.y - 1;
            boolean isHalfHeart = m + 1 >= fireHearts && (fireHeartPoints & 1) == 1; // is odd check

            int u = isHalfHeart ? 9 : 0;

            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    HEART_OVERLAY_TEXTURE,
                    x, y,
                    u, 0,
                    9, 10,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );
        }
    }

    private static int getNumFirePoints(@NotNull LivingEntity player, int maxDisplayHealth) {
        float overheatProgress = player.thermoo$getTemperatureScale();
        if (overheatProgress <= 0f) {
            return 0;
        }
        return Math.round(overheatProgress * maxDisplayHealth);
    }

    private static int getNumFireHeartsFromPoints(int fireHealthPoints) {
        // number of whole hearts
        return MathHelper.ceil(fireHealthPoints / 2.0f);
    }
}
