package com.github.thedeathlycow.thermoo;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;

public class ThermooTest {

    public static void bootstrapRegistries() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    private ThermooTest() {

    }

}
