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

package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.core.v2.builder.ThermooDataComponentMapBuilder;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;
import java.util.function.Supplier;

@Mixin(DataComponentMap.Builder.class)
abstract class DataComponentMapBuilderMixin implements ThermooDataComponentMapBuilder {
    @Shadow
    @Final
    private Reference2ObjectMap<DataComponentType<?>, Object> map;

    @Shadow
    public abstract <T> DataComponentMap.Builder set(DataComponentType<T> dataComponentType, @Nullable T object);

    @Override
    @SuppressWarnings("unchecked")
    @Unique
    public <T> T thermoo$getOrCreate(DataComponentType<T> type, Supplier<T> fallback) {
        if (!this.map.containsKey(type)) {
            T defaultValue = fallback.get();
            Objects.requireNonNull(defaultValue, "Cannot insert null values to component map builder");
            this.set(type, defaultValue);
        }

        return (T) this.map.get(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Unique
    public <T> T thermoo$getOrElse(DataComponentType<T> type, T fallback) {
        return (T) this.map.getOrDefault(type, fallback);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Unique
    public <T> Optional<T> thermoo$get(DataComponentType<T> type) {
        return Optional.ofNullable((T) this.map.get(type));
    }

    @Override
    @Unique
    public <T> List<T> thermoo$getOrEmpty(DataComponentType<List<T>> type) {
        // creating a new array list guarantees that the list in the map is mutable
        List<T> existing = new ArrayList<>(this.thermoo$getOrCreate(type, Collections::emptyList));
        this.set(type, existing);
        return existing;
    }

    @Override
    @Unique
    public boolean thermoo$contains(DataComponentType<?> type) {
        return this.map.containsKey(type);
    }
}