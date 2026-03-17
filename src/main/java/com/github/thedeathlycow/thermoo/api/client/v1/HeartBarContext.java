package com.github.thedeathlycow.thermoo.api.client.v1;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.UnmodifiableView;
import org.joml.Vector2i;

import java.util.SequencedCollection;

/**
 * Stores data associated with the heart bar as it was last drawn.
 */
@ApiStatus.NonExtendable
public interface HeartBarContext {
    /**
     * An unmodifiable sequenced collection containing positions of individual hearts as rendered on the screen in the
     * heart bar.
     * <p>
     * This list is ordered such that earlier hearts represent smaller health values. For vanilla player health, this
     * means it is ordered left-to-right, bottom-to-top. For vanilla mount health, it is right-to-left, bottom-to-top.
     * <p>
     * For non-vanilla heart bars, this could be in some other order.
     */
    @UnmodifiableView
    SequencedCollection<Vector2i> positions();

    /**
     * The current amount of health displayed on the health bar, in units of half-hearts.
     * <p>
     * May exceed the {@link #maxDisplayHalfHearts()} when used with mods that layer multiple heart bars on top of each
     * other, such as Colorful Hearts.
     */
    int currentDisplayHalfHearts();

    /**
     * The maximum allowed size of the heart bar, in units of half-hearts.
     */
    int maxDisplayHalfHearts();
}