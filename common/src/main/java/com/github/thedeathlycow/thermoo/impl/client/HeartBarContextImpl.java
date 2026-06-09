package com.github.thedeathlycow.thermoo.impl.client;

import com.github.thedeathlycow.thermoo.api.client.v1.HeartBarContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector2i;

import java.util.SequencedCollection;

@Environment(EnvType.CLIENT)
public record HeartBarContextImpl(
        SequencedCollection<Vector2i> positions,
        int currentDisplayHalfHearts,
        int maxDisplayHalfHearts
) implements HeartBarContext {
}