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

package com.github.thedeathlycow.thermoo.impl.platform;


import com.github.thedeathlycow.thermoo.impl.Thermoo;

import java.util.ServiceLoader;


public final class ThermooServices {
    /// Platform specific info to query the current environment when Yumi is not sufficient
    public static final ThermooPlatform PLATFORM = load(ThermooPlatform.class);

    /// Platform specific service for custom registry handling, like dynamic registries
    public static final ThermooRegistries REGISTRIES = load(ThermooRegistries.class);

    /// Platform service API for data attachments/components
    public static final ThermooComponents COMPONENTS = load(ThermooComponents.class);

    /// Platform service API for creating game rules
    public static final ThermooGameRules GAME_RULES = load(ThermooGameRules.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz, ThermooServices.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Thermoo.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

    private ThermooServices() {

    }
}