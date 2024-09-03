package com.github.thedeathlycow.thermoo;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;

public class ThermooTest {

    public static void bootstrapRegistries() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
    }

    private ThermooTest() {

    }

}
