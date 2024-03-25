package com.github.thedeathlycow.thermoo.impl;

import net.fabricmc.loader.api.FabricLoader;

public class ThermooIntegrations {

    public static final String COLORFUL_HEARTS_ID = "colorfulhearts";

    public static final String OVERFLOWING_BARS_ID = "overflowingbars";

    public static final String FABRIC_SEASONS_ID = "seasons";

    public static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }


}
