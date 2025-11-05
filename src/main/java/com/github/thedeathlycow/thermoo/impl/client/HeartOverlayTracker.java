package com.github.thedeathlycow.thermoo.impl.client;

import org.joml.Vector2i;

import java.util.Arrays;
import java.util.Objects;
import net.minecraft.Util;

public class HeartOverlayTracker {

    private static final int HEARTS_PER_ROW = 10;

    private Vector2i[] heartPositions = Util.make(() -> {
        var positions = new Vector2i[HEARTS_PER_ROW];
        Arrays.fill(positions, null);
        return positions;
    });

    public void addHeartPosition(int index, int heartX, int heartY) {
        if (index >= heartPositions.length) {
            heartPositions = Arrays.copyOf(heartPositions, getNextSize(index));
        }
        heartPositions[index] = new Vector2i(heartX, heartY);
    }

    public Vector2i[] getHeartPositions() {
        return heartPositions;
    }

    static int getNextSize(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Cannot have index less than 0");
        }
        return index + HEARTS_PER_ROW - (index % HEARTS_PER_ROW);
    }
}
