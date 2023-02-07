package com.github.thedeathlycow.thermoo.api;

import net.minecraft.util.StringIdentifiable;

public enum TemperatureMode implements StringIdentifiable {
    ABSOLUTE("absolute"),
    PASSIVE("passive"),
    ACTIVE("active");

    private final String id;

    TemperatureMode(String id) {
        this.id = id;
    }

    @Override
    public String asString() {
        return this.id;
    }
}
