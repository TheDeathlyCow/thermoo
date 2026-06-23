/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
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

package com.github.thedeathlycow.thermoo.api.core.v2.builder;

import net.minecraft.core.component.DataComponentType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/// Thermoo-provided extensions for [net.minecraft.core.component.DataComponentMap.Builder]. Heavily based on an
/// equivalent interface in Fabric, but abstracted to allow for multi-loader use.
///
/// Note: This interface is automatically implemented on all component map builders via Mixin and interface injection.
public interface ThermooDataComponentMapBuilder {
    /// Gets the current value for the component type in the builder, or creates and adds a new value if it is not present.
    ///
    /// @param type     The component type
    /// @param fallback The supplier for the default data value if the type is not in this map yet. The value given by this supplier
    ///                                                                 may not be null.
    /// @param <T>      The type of the component data
    /// @return Returns the current value in the map builder, or the default value provided by the fallback if not present
    /// @see #thermoo$getOrEmpty(DataComponentType)
    default <T> T thermoo$getOrCreate(DataComponentType<T> type, Supplier<T> fallback) {
        throw new AssertionError("Implemented in Mixin");
    }

    /// Gets the current value for the component type in the builder, or creates and adds a new value if it is not present.
    ///
    /// @param type         The component type
    /// @param defaultValue The default data value if the type is not in this map yet
    /// @param <T>          The type of the component data
    /// @return Returns the current value in the map builder, or the default value if not present
    default <T> T thermoo$getOrAdd(DataComponentType<T> type, T defaultValue) {
        Objects.requireNonNull(defaultValue, "Cannot insert null values to component map builder");
        return thermoo$getOrCreate(type, () -> defaultValue);
    }

    /// Gets the current value for the component type in the builder, or returns the `fallback` if it is not present.
    ///
    /// @param type     The component type
    /// @param fallback The default value to use if no value is associated with the type in the builder.
    /// @param <T>      The type of the component data
    /// @return Returns the current value for the type in the builder, or the default value if not present
    default <T> T thermoo$getOrElse(DataComponentType<T> type, T fallback) {
        throw new AssertionError("Implemented in Mixin");
    }

    /// Gets the current value for the component type in the builder, or returns empty if it is not present.
    ///
    /// @param type The component type
    /// @param <T>  The type of the component data
    /// @return Returns an optional containing the current value for the type in the builder, or empty if not present.
    default <T> Optional<T> thermoo$get(DataComponentType<T> type) {
        throw new AssertionError("Implemented in Mixin");
    }

    /// For list component types specifically, returns a mutable list of values currently held in the builder for the given
    /// component type. If the type is not registered to this builder yet, this will create and add a new empty list to the builder
    /// for the type, and return that.
    ///
    /// @param type The component type. The component must be a list-type.
    /// @param <T>  The type of the component entry data
    /// @return Returns a mutable list of values for the type.
    default <T> List<T> thermoo$getOrEmpty(DataComponentType<List<T>> type) {
        throw new AssertionError("Implemented in Mixin");
    }

    /// Checks if a component type has been registered to this builder.
    ///
    /// @param type The component type to check
    /// @return Returns true if the type has been registered to this builder, false otherwise
    default boolean thermoo$contains(DataComponentType<?> type) {
        throw new AssertionError("Implemented in Mixin");
    }
}