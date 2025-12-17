package com.github.thedeathlycow.thermoo.api.season;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import java.util.Optional;

public enum PolarSeason implements StringRepresentable {
    SUMMER("summer"),
    WINTER("winter");

    public static final Codec<PolarSeason> CODEC = StringRepresentable.fromEnum(PolarSeason::values);

    private final String name;

    PolarSeason(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public static Optional<PolarSeason> getCurrentSeason(Level level, BlockPos pos) {
        return ThermooSeasonEvents.GET_CURRENT_POLAR_SEASON.invoker().getCurrentPolarSeason(level, pos);
    }
}