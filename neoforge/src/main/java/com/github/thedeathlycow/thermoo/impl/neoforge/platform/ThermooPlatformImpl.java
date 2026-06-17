package com.github.thedeathlycow.thermoo.impl.neoforge.platform;

import com.github.thedeathlycow.thermoo.impl.platform.Loader;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooPlatform;

public class ThermooPlatformImpl implements ThermooPlatform {
    @Override
    public Loader getLoader() {
        return Loader.NEOFORGE;
    }
}