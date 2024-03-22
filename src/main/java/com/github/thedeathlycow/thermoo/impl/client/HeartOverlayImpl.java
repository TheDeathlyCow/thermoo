package com.github.thedeathlycow.thermoo.impl.client;

import net.minecraft.util.Util;
import org.joml.Vector2i;

import java.util.Arrays;

public class HeartOverlayImpl {

    public static final HeartOverlayImpl INSTANCE = new HeartOverlayImpl();

    private static final int MAX_OVERLAY_HEARTS = 20;

    private final Vector2i[] heartPositions = Util.make(() -> {
        var positions = new Vector2i[MAX_OVERLAY_HEARTS];
        Arrays.setAll(positions, i -> new Vector2i());
        return positions;
    });

    public void setHeartPosition(int index, int heartX, int heartY) {
        heartPositions[index].x = heartX;
        heartPositions[index].y = heartY;
    }

    public Vector2i[] getHeartPositions() {
        return heartPositions;
    }

    private HeartOverlayImpl() {

    }

}
