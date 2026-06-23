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

package com.github.thedeathlycow.thermoo.impl.neoforge.platform;

import com.github.thedeathlycow.thermoo.impl.platform.ThermooRegistries;
import com.github.thedeathlycow.thermoo.mixin.neoforge.BuiltInRegistriesAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class ThermooRegistriesImpl implements ThermooRegistries {
    public static final List<DynamicRegistry<?>> DYNAMIC_REGISTRIES = new ArrayList<>();

    /*
     * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    @Override
    public <T> Registry<T> createBuiltinRegistry(ResourceKey<Registry<T>> key) {
        WritableRegistry<T> registry = new MappedRegistry<>(key, Lifecycle.stable(), false);
        ResourceKey<?> registryKey = registry.key();

        //noinspection unchecked
        BuiltInRegistriesAccessor.thermoo_getWritableRegistry().register(
                (ResourceKey<WritableRegistry<?>>) registryKey,
                registry,
                RegistrationInfo.BUILT_IN
        );

        return registry;
    }

    @Override
    public <T> void registerDynamicRegistry(ResourceKey<Registry<T>> key, Codec<T> codec) {
        DYNAMIC_REGISTRIES.add(new DynamicRegistry<>(key, codec, false));
    }

    @Override
    public <T> void registerSyncedDynamicRegistry(ResourceKey<Registry<T>> key, Codec<T> codec) {
        DYNAMIC_REGISTRIES.add(new DynamicRegistry<>(key, codec, true));
    }

    @Override
    public void addAlias(Registry<?> registry, Identifier oldId, Identifier newId) {
        registry.addAlias(oldId, newId);
    }

    public record DynamicRegistry<T>(ResourceKey<Registry<T>> key, Codec<T> codec, boolean sync) {
        public void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
            event.dataPackRegistry(key, codec, sync ? codec : null);
        }
    }
}