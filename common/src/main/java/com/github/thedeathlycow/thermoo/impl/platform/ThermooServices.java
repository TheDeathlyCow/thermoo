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