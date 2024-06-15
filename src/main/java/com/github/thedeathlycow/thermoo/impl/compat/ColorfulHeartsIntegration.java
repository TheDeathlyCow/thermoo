package com.github.thedeathlycow.thermoo.impl.compat;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import com.github.thedeathlycow.thermoo.impl.client.HeartOverlayImpl;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;
import terrails.colorfulhearts.api.fabric.ColorfulHeartsApi;
import terrails.colorfulhearts.api.fabric.event.FabHeartEvents;

import java.util.Arrays;

public class ColorfulHeartsIntegration implements ColorfulHeartsApi {

    public ColorfulHeartsIntegration() {
        FabHeartEvents.SINGLE_RENDER.register(event -> HeartOverlayImpl.INSTANCE.setHeartPosition(event.getIndex(), event.getX(), event.getY()));

        FabHeartEvents.POST_RENDER.register(event -> {
            Vector2i[] heartPositions = HeartOverlayImpl.INSTANCE.getHeartPositions();
            int displayHealth = Math.min(event.getHealth(), heartPositions.length);
            int maxDisplayHealth = Math.min(MathHelper.ceil(event.getMaxHealth()), heartPositions.length);

            StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.invoker()
                    .render(
                            event.getGuiGraphics(),
                            event.getPlayer(),
                            heartPositions,
                            displayHealth,
                            maxDisplayHealth
                    );
            Arrays.fill(HeartOverlayImpl.INSTANCE.getHeartPositions(), null);
        });
    }
}
