package com.github.thedeathlycow.thermoo.api.season;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import java.util.Optional;

public enum TropicalSeason implements StringRepresentable {
    DRY("dry"),
    WET("wet");

    public static final Codec<TropicalSeason> CODEC = StringRepresentable.fromEnum(TropicalSeason::values);

    private final String name;

    TropicalSeason(String name) {
        this.name = name;
    }


    @Override
    public String getSerializedName() {
        return this.name;
    }

    /**
     * Shorthand for invoking {@link ThermooSeasonEvents#GET_CURRENT_TROPICAL_SEASON}.
     * <p>
     * Retrieves the current tropical season at a position in the world. If the position queried is not in a tropical
     * biome, or a seasons mod is not loaded, then empty should be returned.
     * <p>
     * If any listener returns a non-empty season, then all further processing is cancelled and that season is returned.
     * <p>
     * Returns empty by default.
     *
     * @param level The world / level to query
     * @param pos   The position in the world to query
     * @return If the queried pos is a tropical area and a seasons mod is loaded, returns one of {@link #DRY} or
     * {@link #WET}
     * @see TemperateSeason for the standard 'temperate' seasons
     */
    public static Optional<TropicalSeason> getCurrentSeason(Level level, BlockPos pos) {
        return ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON.invoker().getCurrentTropicalSeason(level, pos);
    }
}