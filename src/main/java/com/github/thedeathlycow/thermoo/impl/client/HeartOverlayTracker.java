package com.github.thedeathlycow.thermoo.impl.client;

import net.minecraft.util.Util;
import org.joml.Vector2i;

import java.util.Arrays;

public class HeartOverlayTracker {

    private static final int DEFAULT_SIZE = 10;

    private Vector2i[] heartPositions = Util.make(() -> {
        var positions = new Vector2i[DEFAULT_SIZE];
        Arrays.fill(positions, null);
        return positions;
    });

    public void addHeartPosition(int index, int heartX, int heartY) {
        if (index >= heartPositions.length) {
            heartPositions = Arrays.copyOf(heartPositions, index * 2 + 1);
        }
        heartPositions[index] = new Vector2i(heartX, heartY);
    }

    public Vector2i[] getHeartPositions() {
        return heartPositions;
    }
}
