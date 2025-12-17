package com.github.thedeathlycow.thermoo.api.season;

import net.minecraft.util.StringRepresentable;

public interface ThermooSeason extends StringRepresentable {
    default ThermooSeasonState<? extends ThermooSeason> createState() {
        return ThermooSeasonState.of(this, 0f);
    }
}