package com.github.thedeathlycow.thermoo.impl.config;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


@Config(name = "thermoo")
public class ThermooConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public final ThermooUpdateConfig updateConfig = new ThermooUpdateConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public final ThermooEnvironmentConfig environmentConfig = new ThermooEnvironmentConfig();

    public static void updateConfig(ConfigHolder<ThermooConfig> configHolder) {
        ThermooUpdateConfig config = configHolder.getConfig().updateConfig;

        if (config.isConfigUpdatesEnabled() && config.currentConfigVersion != Thermoo.CONFIG_VERSION) {
            config.currentConfigVersion = Thermoo.CONFIG_VERSION;
            configHolder.resetToDefault();
            configHolder.save();

            Thermoo.LOGGER.info("The Thermoo Config has been reset due to an update to the default values. " +
                    "You may disable these updates if you don't want this to happen.");
        }
    }


}
