package com.github.thedeathlycow.thermoo.impl.client;

import com.github.thedeathlycow.thermoo.api.client.HeartBarContext;
import org.joml.Vector2i;

import java.util.SequencedCollection;

public record HeartBarContextImpl(
        SequencedCollection<Vector2i> positions,
        int currentDisplayHalfHearts,
        int maxDisplayHalfHearts
) implements HeartBarContext {
}