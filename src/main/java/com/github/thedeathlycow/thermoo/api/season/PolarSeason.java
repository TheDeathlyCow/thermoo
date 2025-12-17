package com.github.thedeathlycow.thermoo.api.season;

import net.minecraft.util.StringRepresentable;

public enum PolarSeason implements StringRepresentable {
    SUMMER_BRIGHT("summer_bright"),
    WINTER_DARK("winter_dark");

    private final String name;

    PolarSeason(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}