package com.github.thedeathlycow.thermoo.impl.config;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "thermoo.update")
public class ThermooUpdateConfig {

    @ConfigEntry.Gui.Excluded
    public int currentConfigVersion = Thermoo.CONFIG_VERSION;

    @ConfigEntry.Gui.Tooltip(count = 3)
    boolean enableConfigUpdates = true;

    public boolean isConfigUpdatesEnabled() {
        return enableConfigUpdates;
    }

}
