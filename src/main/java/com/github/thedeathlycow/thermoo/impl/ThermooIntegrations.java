package com.github.thedeathlycow.thermoo.impl;

import net.fabricmc.loader.api.FabricLoader;

public class ThermooIntegrations {

    public static final String COLORFUL_HEARTS_ID = "colorfulhearts";

    public static final String OVERFLOWING_BARS_ID = "overflowingbars";

    public static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }


}
