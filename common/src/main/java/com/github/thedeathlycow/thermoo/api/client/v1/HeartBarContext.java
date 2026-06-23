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